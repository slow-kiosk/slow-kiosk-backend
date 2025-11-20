package com.slowkiosk.spring.service;

import com.slowkiosk.spring.dto.ai.AnalyzeRequestDto;
import com.slowkiosk.spring.dto.ai.AnalyzeResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiPythonService {

    private final WebClient aiPythonWebClient;

    /**
     * Python AI 서버(/analyze)에 상황 정보를 보내고 분석 결과를 받습니다.
     */
    public AnalyzeResponseDto analyzeRequest(AnalyzeRequestDto requestDto) {
        log.info("AI 요청: text='{}', scene='{}'", requestDto.getText(), requestDto.getScene());

        try {
            AnalyzeResponseDto response = aiPythonWebClient.post()
                    .uri("/analyze") // API 스펙 문서 기준 엔드포인트
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestDto)
                    .retrieve()
                    .bodyToMono(AnalyzeResponseDto.class)
                    .block(); // 동기 처리

            log.info("AI 응답: {}", response);
            return response;

        } catch (Exception e) {
            log.error("AI 서버 호출 실패: {}", e.getMessage());
            // 실패 시 기본 응답 생성 (NullPointerException 방지)
            AnalyzeResponseDto fallback = new AnalyzeResponseDto();
            fallback.setAssistantText("죄송합니다. 인공지능 서버와 연결할 수 없습니다.");
            fallback.setNextScene(requestDto.getScene()); // 현재 상태 유지
            fallback.setActions(java.util.Collections.emptyList());
            return fallback;
        }
    }
}