package com.slowkiosk.spring.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    // application.yml에서 Python 서버 URL을 주입받습니다.
    @Value("${ai.python.server.url}")
    private String pythonServerUrl;

    /**
     * Python AI 서버와 통신하기 위한 WebClient 빈을 생성합니다.
     * @return WebClient 인스턴스
     */
    @Bean
    // 빈의 이름을 "aiPythonWebClient"로 명확하게 지정합니다.
    public WebClient aiPythonWebClient() {

        // 1. 타임아웃 설정을 위한 HttpClient 객체 생성
        HttpClient httpClient = HttpClient.create()
                // (1) Connection Timeout: 서버와 연결을 시도하는 최대 시간 (e.g., 5초)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                // (2) Response Timeout: 연결 후, 서버로부터 응답을 기다리는 최대 시간 (AI용으로 넉넉하게)
                .responseTimeout(Duration.ofSeconds(30));

        // 2. WebClient 빌드
        return WebClient.builder()
                // (중요) 모든 요청에 Python 서버의 기본 URL을 사용합니다.
                .baseUrl(pythonServerUrl)
                // 위에서 설정한 타임아웃이 적용된 HttpClient를 WebClient에 연결합니다.
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}