package com.slowkiosk.spring.service;

import com.slowkiosk.spring.dto.KioskRequest;
import com.slowkiosk.spring.dto.KioskResponse;
import com.slowkiosk.spring.dto.OrderDto;
import com.slowkiosk.spring.entity.Menu;
import com.slowkiosk.spring.repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrchestrationService {

    // 1. AI 분석 서비스
    private final AiPythonService aiPythonService;

    // 2. 임시 장바구니 (세션 스코프)
    private final CartService cartService;

    // 3. 메뉴 조회용 (DB)
    private final MenuRepository menuRepository;

    // 4. 최종 주문 생성용 (DB)
    private final OrderService orderService;

    /**
     * 키오스크의 다음 상태를 결정하고 응답을 반환합니다.
     * (중요) 이 메서드는 여러 DB 작업을 하므로 트랜잭션으로 묶습니다.
     */
    @Transactional
    public KioskResponse processNextStep(KioskRequest request) {
        log.info("Request received: {}", request);
        log.info("Current Cart Items: {}", cartService.getCartItems());

        // --- 1. (가짜) AI 분석 ---
        // TODO: aiPythonService.analyzeText(...) 실제 구현
        //String llmAction = "ADD_ITEM"; // (aiPythonService.analyzeText()가 반환했다고 가정)
        //String llmItemName = request.getUserText(); // (간단하게 STT 텍스트를 메뉴 이름으로 가정)
        // -------------------------

        // (테스트용 가짜 AI 분석)
        String userText = request.getUserText();
        String llmAction;
        String llmItemName = userText; // STT 텍스트를 메뉴 이름으로 가정

        if (request.getCurrentState().equals("WELCOME")) {
            llmAction = "START";
        } else if (request.getCurrentState().equals("CONFIRM_ORDER") && userText.contains("결제")) {
            llmAction = "CONFIRM";
        } else if (userText.contains("아니") || userText.contains("됐어")) {
            llmAction = "SKIP";
        } else {
            llmAction = "ADD_ITEM"; // 기본값은 아이템 추가
        }

        // "상태 기계" 로직 실행
        switch (request.getCurrentState()) {

            case "WELCOME":
                if ("START".equals(llmAction)) { // "주문할게요"
                    return KioskResponse.builder()
                            .newState("SELECT_BURGER")
                            .spokenResponse("안녕하세요. 햄버거를 골라주세요...")
                            .build();
                }
                break; // START 아니면 default로 감


            case "SELECT_BURGER":
            case "SELECT_DRINK":
            case "SELECT_SIDE":
                // 햄버거/음료/사이드 선택 로직 (공통 처리)
                if ("ADD_ITEM".equals(llmAction)) {
                    try {
                        // 1. (DB) AI가 분석한 이름으로 메뉴 조회
                        Menu menu = menuRepository.findByName(llmItemName)
                                .orElseThrow(() -> new EntityNotFoundException("메뉴 없음: " + llmItemName));

                        // 2. (Session) 장바구니에 담기
                        cartService.addItem(menu, 1);
                        log.info("장바구니 추가: {}, 현재 상태: {}", llmItemName, cartService.getCartItems());

                        // 3. 다음 상태 결정 (예: 햄버거 -> 음료, 음료 -> 사이드)
                        String nextState = determineNextState(request.getCurrentState());

                        return KioskResponse.builder()
                                .newState(nextState)
                                .spokenResponse(llmItemName + "를 담았습니다. " + getSpokenPromptForState(nextState))
                                .updatedCart(cartService.getCartItems()) // (UI 업데이트용)
                                .build();

                    } catch (EntityNotFoundException e) {
                        log.warn(e.getMessage());
                        return KioskResponse.builder()
                                .newState(request.getCurrentState()) // 상태 유지
                                .spokenResponse("죄송합니다. '" + llmItemName + "'라는 메뉴를 찾을 수 없습니다. 다시 말씀해주세요.")
                                .updatedCart(cartService.getCartItems())
                                .build();
                    }
                }
                // TODO: "건너뛰기(SKIP)" 등 다른 LLM 액션 처리
                break; // switch 탈출

            case "CONFIRM_ORDER": // 사용자가 "결제할게요"라고 말한 상태
                // (가정) llmAction이 "CONFIRM"이라고 가정

                // 1. (Session) 세션 장바구니에서 주문 DTO 생성
                OrderDto.CreateRequest orderRequest = cartService.createOrderRequestDto();

                if (orderRequest.getItems().isEmpty()) {
                    return KioskResponse.builder()
                            .newState("WELCOME") // 장바구니가 비었으면 처음으로
                            .spokenResponse("장바구니가 비어있습니다. 주문을 처음부터 시작해주세요.")
                            .build();
                }

                // 2. (DB) OrderService를 호출하여 DB에 최종 주문 생성
                OrderDto.Response finalOrder = orderService.createOrder(orderRequest);

                // 3. (Session) 세션 장바구니 비우기
                cartService.clearCart();

                return KioskResponse.builder()
                        .newState("ORDER_COMPLETE")
                        .spokenResponse("주문이 완료되었습니다. 총 금액은 " + finalOrder.getTotalPrice() + "원 입니다.")
                        .updatedCart(null)
                        .build();

            default:
                log.warn("알 수 없는 상태입니다: {}", request.getCurrentState());
                return KioskResponse.builder()
                        .newState("WELCOME")
                        .spokenResponse("죄송합니다. 처음부터 다시 시도해주세요.")
                        .build();
        }

        // (ADD_ITEM 외의 액션 처리 시)
        return KioskResponse.builder()
                .newState(request.getCurrentState()) // 기본값: 상태 유지
                .spokenResponse("죄송합니다. 잘 이해하지 못했어요.")
                .updatedCart(cartService.getCartItems())
                .build();
    }

    // --- Helper Methods ---

    private String determineNextState(String currentState) {
        if ("SELECT_BURGER".equals(currentState)) {
            return "SELECT_DRINK";
        }
        if ("SELECT_DRINK".equals(currentState)) {
            return "SELECT_SIDE";
        }
        return "CONFIRM_ORDER"; // 사이드 메뉴 다음은 주문 확인
    }

    private String getSpokenPromptForState(String newState) {
        if ("SELECT_DRINK".equals(newState)) {
            return "음료를 선택해주세요.";
        }
        if ("SELECT_SIDE".equals(newState)) {
            return "사이드 메뉴를 선택하시겠어요?";
        }
        return "추가로 주문하실 것이 있나요? 없으시면 '결제'라고 말씀해주세요.";
    }
}