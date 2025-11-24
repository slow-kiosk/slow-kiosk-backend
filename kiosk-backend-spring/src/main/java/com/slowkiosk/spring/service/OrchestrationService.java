package com.slowkiosk.spring.service;

import com.slowkiosk.spring.dto.KioskRequest;
import com.slowkiosk.spring.dto.KioskResponse;
import com.slowkiosk.spring.dto.OrderDto;
import com.slowkiosk.spring.dto.ai.AnalyzeRequestDto;
import com.slowkiosk.spring.dto.ai.AnalyzeResponseDto;
import com.slowkiosk.spring.entity.Menu;
import com.slowkiosk.spring.repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrchestrationService {

    private final AiPythonService aiPythonService;
    private final CartService cartService;
    private final MenuRepository menuRepository;
    private final OrderService orderService;

    /**
     * 키오스크의 다음 상태를 결정하고 응답을 반환합니다.
     * (중요) WebSocket SessionId를 받아 사용자별 장바구니를 구분합니다.
     */
    @Transactional
    public KioskResponse processNextStep(String sessionId, KioskRequest request) {
        log.info("User Input [Session: {}]: {}", sessionId, request.getUserText());

        // 1. 현재 상황에 필요한 데이터 수집 (Menu, Cart)
        List<Menu> allMenus = menuRepository.findAll();
        Map<Long, Integer> currentCartMap = cartService.getCart(sessionId);

        // 2. AI 요청 DTO 생성 (API 스펙에 맞춰 데이터 변환)
        AnalyzeRequestDto aiRequest = AnalyzeRequestDto.builder()
                .text(request.getUserText())
                .scene(request.getCurrentState())
                .cart(buildAiCartDto(currentCartMap)) // 현재 장바구니 상태 첨부
                .menu(buildAiMenuDtoList(allMenus))   // [핵심] 매핑된 메뉴 정보 첨부
                .build();

        // 3. Python AI 서버 호출 (실제 분석 요청)
        AnalyzeResponseDto aiResponse = aiPythonService.analyzeRequest(aiRequest);
        log.info("AI Analysis Result: {}", aiResponse);

        // 4. AI의 판단(Actions) 실행 (장바구니 추가/삭제 등)
        if (aiResponse.getActions() != null) {
            for (AnalyzeResponseDto.KioskAction action : aiResponse.getActions()) {
                processAction(sessionId, action);
            }
        }

        // 5. 주문 종료(결제) 판단
        // AI가 'should_finish'를 true로 보냈거나, 다음 화면이 'ORDER_COMPLETE'인 경우
        if (aiResponse.isShouldFinish() || "ORDER_COMPLETE".equals(aiResponse.getNextScene())) {
            return handleOrderCompletion(sessionId, aiResponse);
        }

        // 6. 일반 응답 반환 (AI가 정해준 멘트와 다음 화면 사용)
        return KioskResponse.builder()
                .newState(aiResponse.getNextScene()) // AI가 판단한 다음 화면
                .spokenResponse(aiResponse.getAssistantText()) // AI가 생성한 답변
                .updatedCart(cartService.getCart(sessionId)) // 갱신된 장바구니
                .build();
    }

    // --- 내부 로직 ---

    /**
     * AI의 Action(추가/삭제)을 실제 CartService에 반영
     */
    private void processAction(String sessionId, AnalyzeResponseDto.KioskAction action) {
        if ("NONE".equals(action.getType()) || action.getMenuId() == null) {
            return;
        }

        try {
            // Python은 String ID를 쓰지만, DB는 Long ID를 쓰므로 변환
            Long menuId = Long.parseLong(action.getMenuId());
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new EntityNotFoundException("Menu not found: " + menuId));

            int qty = (action.getQty() != null) ? action.getQty() : 1;

            switch (action.getType()) {
                case "ADD_ITEM":
                    cartService.addItem(sessionId, menu, qty);
                    log.info("Cart Update [Add]: {} {}ea", menu.getName(), qty);
                    break;
                case "REMOVE_ITEM":
                    // 수량이 음수면 빼기 로직으로 동작하게 하거나, 별도 remove 메서드 구현
                    cartService.addItem(sessionId, menu, -qty);
                    log.info("Cart Update [Remove]: {} {}ea", menu.getName(), qty);
                    break;
                default:
                    log.warn("Unknown Action Type: {}", action.getType());
            }

        } catch (NumberFormatException e) {
            log.error("Invalid Menu ID format from AI: {}", action.getMenuId());
        } catch (EntityNotFoundException e) {
            log.error("Menu ID from AI not found in DB: {}", action.getMenuId());
        }
    }

    /**
     * 주문 완료 처리 (DB 저장 및 장바구니 초기화)
     */
    private KioskResponse handleOrderCompletion(String sessionId, AnalyzeResponseDto aiResponse) {
        // 1. 주문 DTO 생성
        OrderDto.CreateRequest orderRequest = cartService.createOrderRequestDto(sessionId);

        if (orderRequest.getItems().isEmpty()) {
            return KioskResponse.builder()
                    .newState("WELCOME")
                    .spokenResponse("장바구니가 비어있어 주문을 종료합니다.")
                    .build();
        }

        // 2. DB에 최종 주문 저장
        OrderDto.Response finalOrder = orderService.createOrder(orderRequest);

        // 3. 장바구니 비우기
        cartService.clearCart(sessionId);

        // 4. 최종 멘트에 금액 정보 추가
        String finalMessage = aiResponse.getAssistantText() +
                " 총 금액은 " + finalOrder.getTotalPrice() + "원 입니다.";

        return KioskResponse.builder()
                .newState("ORDER_COMPLETE")
                .spokenResponse(finalMessage)
                .updatedCart(null)
                .build();
    }

    // --- DTO 변환 헬퍼 ---

    // 장바구니 Map -> AI 요청용 DTO 변환
    private AnalyzeRequestDto.AiCartDto buildAiCartDto(Map<Long, Integer> cartMap) {
        List<AnalyzeRequestDto.AiCartItemDto> items = cartMap.entrySet().stream()
                .map(entry -> AnalyzeRequestDto.AiCartItemDto.builder()
                        .menuId(String.valueOf(entry.getKey()))
                        .qty(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        return AnalyzeRequestDto.AiCartDto.builder().items(items).build();
    }

    // [핵심 수정] DB 메뉴 리스트 -> AI 요청용 DTO 변환 (데이터 매핑 로직)
    private List<AnalyzeRequestDto.AiMenuDto> buildAiMenuDtoList(List<Menu> menus) {
        return menus.stream()
                .map(m -> {
                    // 전략: 영양 정보와 설명을 파이썬 서버의 'ingredients_ko' 필드에 텍스트로 합쳐서 보냄
                    String combinedInfo = String.format("%s / %s",
                            m.getDescription() != null ? m.getDescription() : "",
                            m.getNutrition() != null ? m.getNutrition() : "");

                    // 태그 문자열("a,b")을 리스트(["a","b"])로 변환
                    List<String> tagList = (m.getTags() != null && !m.getTags().isEmpty())
                            ? List.of(m.getTags().split(","))
                            : List.of();

                    return AnalyzeRequestDto.AiMenuDto.builder()
                            .menuId(String.valueOf(m.getId()))
                            .name(m.getName())
                            .category(m.getCategory())
                            .price((int) m.getPrice())
                            .tags(tagList)
                            // [핵심] 파이썬 서버의 ingredients_ko 필드에 모든 정보를 몰아 넣음
                            .ingredients_ko(combinedInfo)
                            .build();
                })
                .collect(Collectors.toList());
    }
}