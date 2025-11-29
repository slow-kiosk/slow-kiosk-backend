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
     * WebSocket SessionId를 사용하여 사용자별 장바구니를 구분합니다.
     */
    @Transactional
    public KioskResponse processNextStep(String sessionId, KioskRequest request) {
        log.info("User Input [Session: {}]: {}", sessionId, request.getUserText());

        // 1. 데이터 수집
        List<Menu> allMenus = menuRepository.findAll();
        Map<Long, Integer> currentCartMap = cartService.getCart(sessionId);

        // [수정] 대화 내역이 null이면 빈 리스트로 변환 (안전장치)
        List<Map<String, String>> historyData = request.getHistory();
        if (historyData == null) {
            historyData = List.of(); // null 대신 [] (빈 리스트) 사용
        }

        // 2. AI 요청 DTO 생성
        AnalyzeRequestDto aiRequest = AnalyzeRequestDto.builder()
                .text(request.getUserText())
                .scene(request.getCurrentState())
                .cart(buildAiCartDto(currentCartMap))
                .menu(buildAiMenuDtoList(allMenus))
                .history(historyData) // [수정] 안전하게 변환된 history 전달
                .build();

        // 3. Python AI 서버 호출
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
                    // 수량이 음수면 빼기 로직으로 동작
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

    // Menu 엔티티 -> AI 요청용 메뉴 정보 변환
    private List<AnalyzeRequestDto.AiMenuDto> buildAiMenuDtoList(List<Menu> menus) {
        return menus.stream()
                .map(m -> {
                    List<String> tagList = (m.getTags() != null && !m.getTags().isEmpty())
                            ? List.of(m.getTags().split(","))
                            : List.of();

                    // 1. 영양 정보와 알레르기 정보 분리 로직 (DataInitializer 포맷 기준)
                    // 예: "720kcal... / 알레르기: 밀,대두" 에서 "/" 를 기준으로 나눔
                    String fullInfo = m.getNutrition();
                    String nutritionSummary = fullInfo;
                    String allergyWarning = "";

                    if (fullInfo != null && fullInfo.contains("/")) {
                        String[] parts = fullInfo.split("/", 2); // 최대 2개로 분할
                        nutritionSummary = parts[0].trim();
                        allergyWarning = parts.length > 1 ? parts[1].trim() : "";
                    }

                    return AnalyzeRequestDto.AiMenuDto.builder()
                            .menuId(String.valueOf(m.getId()))
                            .name(m.getName())
                            .category(m.getCategory())
                            .price((int) m.getPrice())
                            .tags(tagList)

                            // [수정 완료] Java DTO의 필드명(CamelCase) 사용
                            .ingredientsKo(m.getDescription())
                            .description(m.getDescription())

                            // [수정 완료] 분리된 영양 정보 매핑
                            .nutritionSummaryKo(nutritionSummary)
                            .allergyWarningKo(allergyWarning)

                            .build();
                })
                .collect(Collectors.toList());
    }
}