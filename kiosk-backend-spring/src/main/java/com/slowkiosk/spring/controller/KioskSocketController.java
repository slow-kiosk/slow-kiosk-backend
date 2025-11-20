package com.slowkiosk.spring.controller;

import com.slowkiosk.spring.dto.KioskRequest;
import com.slowkiosk.spring.dto.KioskResponse;
import com.slowkiosk.spring.service.OrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header; // 추가
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class KioskSocketController {

    private final OrchestrationService orchestrationService;

    @MessageMapping("/kiosk/message")
    @SendTo("/sub/kiosk/response")
    public KioskResponse handleKioskMessage(
            @Header("simpSessionId") String sessionId, // (중요) 세션 ID 추출
            KioskRequest request) {

        // 세션 ID를 서비스로 전달
        return orchestrationService.processNextStep(sessionId, request);
    }
}