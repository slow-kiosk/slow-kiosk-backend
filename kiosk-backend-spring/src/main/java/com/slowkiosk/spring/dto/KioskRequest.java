package com.slowkiosk.spring.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class KioskRequest {

    private String currentState;
    private String userText;
    private String clientKey; // 사용자 구분용 키

    // [추가] 대화 내역 (예: [{"role": "user", "content": "..."}])
    private List<Map<String, String>> history;
}