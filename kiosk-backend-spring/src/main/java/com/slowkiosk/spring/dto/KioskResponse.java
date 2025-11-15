package com.slowkiosk.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder // Service에서 응답을 만들 때 편리합니다.
@AllArgsConstructor // Builder를 위한 생성자
public class KioskResponse {

    /**
     * React가 다음으로 전환해야 할 UI 상태
     * 예: "SELECT_DRINK"
     */
    private String newState;

    /**
     * TTS(텍스트->음성)가 사용자에게 재생할 응답 메시지
     * 예: "음료를 선택해주세요."
     */
    private String spokenResponse;

    /**
     * (선택) 새 상태의 UI를 그리기 위해 필요한 데이터 (예: 음료 메뉴 목록)
     */
    private Object uiData;

    /**
     * (선택) 업데이트된 장바구니 정보
     */
    private Object updatedCart;
}