# 🍚 느린 키오스크 (Slow Kiosk) - Spring Boot 백엔드

디지털 소외 계층(고령층)을 위한 음성 기반 키오스크 프로젝트의 Spring Boot 백엔드 서버입니다.

이 서버는 React 프론트엔드와 Python AI 서버 사이에서 **오케스트레이터(Orchestrator)** 역할을 수행합니다.

---

## 🏛️ 주요 아키텍처

본 서버는 **하이브리드(Hybrid) 아키텍처**를 사용합니다.

* **REST API (HTTP):** 오디오 파일 업로드(STT), 메뉴/주문 데이터 CRUD 등 용량이 크거나 일회성인 작업을 처리합니다.
* **WebSocket (STOMP):** 음성 대화의 '상태 기계(State Machine)'를 관리하고, 장바구니 업데이트 등 실시간 UI 변경을 처리합니다.

---

## 🛠️ 사용된 주요 기술 (Tech Stack)

* **Core Framework**: Spring Boot 3.x (Java 17)
* **Async & Real-time**: Spring WebSocket (STOMP), Spring WebFlux (`WebClient`)
* **Database**: Spring Data JPA
* **Local Test DB**: H2 In-Memory Database
* **State Management**: `@SessionScope` (임시 장바구니)
* **API Docs**: SpringDoc (Swagger UI)

---

## 🚀 로컬 실행 및 테스트

1.  프로젝트를 IntelliJ(또는 IDE)에서 실행합니다.
2.  `application.properties`에 정의된 대로 H2 인메모리 DB가 자동으로 실행됩니다.
3.  `DataInitializer`가 실행되며 테스트용 메뉴 3개(치즈버거, 콜라, 프렌치 프라이)를 자동으로 DB에 삽입합니다.

> **H2 콘솔 (DB 확인):** `http://localhost:8080/h2-console`
>
> **Swagger UI (API 테스트):** `http://localhost:8080/swagger-ui/index.html`

---

## 🔌 주요 API 엔드포인트

### REST API

* `POST /api/speech/stt`: (React) 오디오 파일을 전송하여 STT(음성->텍스트)를 요청합니다.
* `GET /api/menu`: (React) 모든 메뉴 또는 특정 카테고리의 메뉴 목록을 조회합니다.
* `POST /api/orders`: (Spring) '결제' 시점에 최종 주문을 DB에 생성합니다.
* *(기타 `Menu` CRUD API)*

### WebSocket API

* **연결 Handshake**: `ws://localhost:8080/ws-kiosk`
* **메시지 발행 (Client → Server)**:
    * Topic: `/pub/kiosk/message`
    * Payload: `KioskRequest` DTO (JSON)
        ```json
        { "currentState": "...", "userText": "..." }
        ```
* **메시지 구독 (Server → Client)**:
    * Topic: `/sub/kiosk/response`
    * Payload: `KioskResponse` DTO (JSON)
        ```json
        { "newState": "...", "spokenResponse": "...", "updatedCart": ... }
        ```

---

## 📦 연동 서버

* **Python AI 서버**: `http://localhost:8000` (기본 설정)
    * `/stt`: STT 처리
    * `/analyze`: LLM 상태 분석 (가정)
