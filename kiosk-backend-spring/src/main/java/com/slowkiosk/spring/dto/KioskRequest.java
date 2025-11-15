package com.slowkiosk.spring.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString // 로그 확인용
public class KioskRequest {

    /**
     * React UI가 알고 있는 현재 주문 단계
     * 예: "WELCOME", "SELECT_BURGER", "SELECT_DRINK", "CONFIRM_ORDER"
     */
    private String currentState;

    /**
     * [REST API]를 통해 STT가 완료된 텍스트
     */
    private String userText;
}