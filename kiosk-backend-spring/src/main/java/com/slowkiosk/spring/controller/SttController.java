package com.slowkiosk.spring.controller;

import com.slowkiosk.spring.dto.SttResponse;
import com.slowkiosk.spring.service.AiPythonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/speech")
@RequiredArgsConstructor
public class SttController {

    private final AiPythonService aiPythonService;

    /**
     * React로부터 오디오 파일(MultipartFile)을 받아 STT를 처리합니다.
     * @param audioFile React가 'audio'라는 키로 보낸 오디오 파일
     * @return STT가 완료된 텍스트 DTO (e.g., {"text": "치즈버거"})
     */
    @PostMapping("/stt")
    public ResponseEntity<SttResponse> speechToText(
            @RequestParam("audio") MultipartFile audioFile) {

        // (가정) 오디오 파일을 Python 서버로 보내고 텍스트를 받아옵니다.
        // 실제로는 WebClient를 사용한 비동기 호출이 될 것입니다.
        String userText = aiPythonService.getTextFromAudio(audioFile);

        // React에게 텍스트만 응답
        return ResponseEntity.ok(new SttResponse(userText));
    }
}