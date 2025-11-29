package com.slowkiosk.spring.controller;

import com.slowkiosk.spring.dto.KioskRequest;
import com.slowkiosk.spring.dto.KioskResponse;
import com.slowkiosk.spring.service.OrchestrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // 로그용
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate; // [필수]
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class KioskSocketController {

    private final OrchestrationService orchestrationService;
    private final SimpMessagingTemplate messagingTemplate; // [추가] 우편 배달부

    @MessageMapping("/kiosk/message")
    // @SendTo(...)  <-- [삭제] 이제 공용 주소로 보내지 않습니다.
    public void handleKioskMessage(
            @Header("simpSessionId") String sessionId,
            KioskRequest request) {

        // 1. 서비스 로직 수행
        KioskResponse response = orchestrationService.processNextStep(sessionId, request);

        // 2. 답장 보낼 주소 만들기 (/sub/kiosk/response/{clientKey})
        String clientKey = request.getClientKey();
        if (clientKey == null || clientKey.isEmpty()) {
            clientKey = "default"; // 키가 없으면 기본값 (예외 방지)
        }

        String destination = "/sub/kiosk/response/" + clientKey;

        log.info("Sending response to: {}", destination);

        // 3. 배달부를 통해 그 주소로만 쏘기
        messagingTemplate.convertAndSend(destination, response);
    }
}