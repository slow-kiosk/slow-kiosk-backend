package com.slowkiosk.spring.controller;

import com.slowkiosk.spring.dto.KioskRequest;
import com.slowkiosk.spring.dto.KioskResponse;
import com.slowkiosk.spring.service.OrchestrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class KioskSocketController {

    private final OrchestrationService orchestrationService;

    @MessageMapping("/kiosk/message")
    @SendTo("/sub/kiosk/response") // [수정] 복잡한 ID 없이 공용 채널로 방송 (Broadcast)
    public KioskResponse handleKioskMessage(
            @Header("simpSessionId") String sessionId,
            KioskRequest request) {

        // 서비스 로직 수행 (장바구니는 sessionId로 구분되므로 섞이지 않음)
        return orchestrationService.processNextStep(sessionId, request);
    }
}