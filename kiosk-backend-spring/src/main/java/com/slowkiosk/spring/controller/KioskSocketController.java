package com.slowkiosk.spring.controller;

import com.slowkiosk.spring.dto.KioskRequest;
import com.slowkiosk.spring.dto.KioskResponse;
import com.slowkiosk.spring.service.OrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class KioskSocketController {

    private final OrchestrationService orchestrationService;

    /**
     * React가 WebSocket으로 "/pub/kiosk/message" 경로에 KioskRequest를 보냅니다.
     * (WebSocketConfig의 "/pub" + 아래의 "/kiosk/message")
     *
     * @param request (React가 보낸 JSON이 KioskRequest 객체로 변환됨)
     * @return KioskResponse (이 반환값은 "/sub/kiosk/response" 토픽을 구독하는 React에게 전송됨)
     */
    @MessageMapping("/kiosk/message") // <-- /api 제거, 'kiosk'로 시작
    @SendTo("/sub/kiosk/response")  // <-- /api 제거, 일관되게 '/sub'로 시작
    public KioskResponse handleKioskMessage(KioskRequest request) {

        return orchestrationService.processNextStep(request);
    }
}