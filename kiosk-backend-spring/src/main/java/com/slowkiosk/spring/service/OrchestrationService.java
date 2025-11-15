package com.slowkiosk.spring.service;

import com.slowkiosk.spring.dto.KioskRequest;
import com.slowkiosk.spring.dto.KioskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrchestrationService {

    // (의존성 변경) STT 서비스가 아닌, LLM 분석을 위한 서비스로 주입
    private final AiPythonService aiPythonService;

    public KioskResponse processNextStep(KioskRequest request) {
        log.info("Request received: {}", request);

        // 1. (실제 LLM 분석)
        // STT가 완료된 텍스트를 Python 서버로 보내 LLM 분석을 요청합니다.
        // TODO: aiPythonService.analyzeText(...) 실제 구현
        String llmAction = aiPythonService.analyzeText(request.getUserText(), request.getCurrentState());

        // (가짜 LLM 분석 - 임시)
        String llmItemName = request.getUserText(); // (간단하게 STT 텍스트를 그대로 쓴다고 가정)
        // String llmAction = "ADD_ITEM"; // (aiPythonService가 반환했다고 가정)

        // 2. "상태 기계" 로직 실행
        // (이하 로직은 Base64를 사용했을 때와 완벽히 동일합니다.)
        switch (request.getCurrentState()) {
            case "WELCOME":
                return KioskResponse.builder()
                        .newState("SELECT_BURGER")
                        .spokenResponse("안녕하세요. 햄버거를 골라주세요...")
                        .build();

            case "SELECT_BURGER":
                if ("ADD_ITEM".equals(llmAction)) {
                    log.info("장바구니에 {} 추가 (가상)", llmItemName);
                    return KioskResponse.builder()
                            .newState("SELECT_DRINK")
                            .spokenResponse(llmItemName + "를 담았습니다. 음료를 선택해주세요.")
                            .build();
                } else {
                    return KioskResponse.builder()
                            .newState("SELECT_BURGER")
                            .spokenResponse("죄송합니다. 먼저 햄버거를 선택해주세요.")
                            .build();
                }

                // ... (SELECT_DRINK, SELECT_SIDE 등) ...

            default:
                return KioskResponse.builder()
                        .newState("WELCOME")
                        .spokenResponse("죄송합니다. 처음부터 다시 시도해주세요.")
                        .build();
        }
    }
}