package com.slowkiosk.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiPythonService {

    private final WebClient webClient;

    /**
     * Python AI 서버를 호출하여 오디오 파일을 텍스트로 변환합니다.
     */
    public String getTextFromAudio(MultipartFile audioFile) {
        log.info("Requesting STT for file: {}", audioFile.getOriginalFilename());

        // -----------------------------------------------------------
        // (가짜 AI 분석) - 지금은 Python 서버가 없으므로, AI 응답을 흉내 냅니다.
        // TODO: WebClient를 사용해 실제 Python 서버(/stt)로 audioFile을 전송하는 로직 구현
        /*
        return webClient.post()
                .uri(pythonServerUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("audio", audioFile.getResource()))
                .retrieve()
                .bodyToMono(SttResponse.class)
                .map(SttResponse::getText)
                .block(); // (실제로는 비동기 Mono<String>으로 처리하는 것이 더 좋습니다)
        */
        // -----------------------------------------------------------

        // (임시) React가 보낸 파일 이름에 따라 가짜 텍스트 반환 (테스트용)
        if (audioFile.getOriginalFilename().contains("burger")) {
            return "치즈버거";
        } else if (audioFile.getOriginalFilename().contains("coke")) {
            return "콜라";
        }
        return "주문할게요";
    }

    // TODO: 추후 OrchestrationService가 LLM을 호출할 때 사용할 메서드
    public String analyzeText(String userText, String currentState) {
        log.info("Requesting LLM analysis for text: {} in state: {}", userText, currentState);
        // ... (Python 서버의 /analyze 엔드포인트 호출 로직) ...
        return "ADD_ITEM"; // (임시)
    }
}