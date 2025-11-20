package com.slowkiosk.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // WebSocket 메시지 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 1. 메시지 브로커 설정 (Spring -> React)
        //    React가 구독(subscribe)할 토픽의 prefix를 설정
        //    예: React는 "/sub/kiosk/response"를 구독
        registry.enableSimpleBroker("/sub");

        // 2. 메시지 핸들러 설정 (React -> Spring)
        //    React가 메시지를 보낼(publish) 때 사용할 prefix를 설정
        //    예: React는 "/pub/kiosk/message"로 메시지 전송
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // React가 Spring 서버의 WebSocket에 처음 연결할 때 사용할 엔드포인트
        // 예: const socket = new SockJS('http://localhost:8080/ws-kiosk');
        registry.addEndpoint("/ws-kiosk")
                .setAllowedOriginPatterns("*") // (중요) React 앱(localhost:3000)의 CORS 허용
                .withSockJS(); // SockJS는 WebSocket을 지원하지 않는 브라우저를 위한 폴백(fallback)
    }
}