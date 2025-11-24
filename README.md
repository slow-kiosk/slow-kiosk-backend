# Slow Kiosk - Spring Boot Backend

디지털 소외 계층, 특히 고령층을 위한 음성 대화형 키오스크의 백엔드 서버입니다.
사용자의 음성 주문을 실시간으로 처리하고, Python AI 서버와 연동하여 자연스러운 대화 흐름을 제어하는 오케스트레이터(Orchestrator) 역할을 수행합니다.

## 시스템 아키텍처 (Hybrid Architecture)

이 프로젝트는 반응 속도와 효율성을 위해 브라우저 기반 AI와 서버 기반 AI를 혼합하여 사용합니다.

* Frontend (React):
    * STT (Speech-to-Text): 브라우저 내장 Web Speech API를 사용하여 음성을 텍스트로 실시간 변환합니다.
    * TTS (Text-to-Speech): 서버의 응답 텍스트를 브라우저 내장 음성으로 읽어줍니다.
* Backend (Spring Boot):
    * WebSocket (STOMP): 프론트엔드와 실시간으로 텍스트 메시지 및 상태(State)를 주고받습니다.
    * Orchestrator: 대화의 문맥(Context)과 장바구니 상태를 관리하며, AI 서버에 분석을 요청합니다.
* AI Server (Python):
    * LLM (OpenAI): 사용자의 의도를 파악하고, 다음 주문 단계와 답변 멘트를 생성합니다.

## 기술 스택

* Framework: Spring Boot 3.x (Java 17)
* Communication:
    * WebSocket: spring-websocket, Stomp (실시간 상태 동기화)
    * HTTP Client: Spring WebClient (Python AI 서버 통신)
* Database:
    * H2 Database: 로컬 개발 및 테스트용 인메모리 DB
    * Spring Data JPA: 데이터 접근 계층
* API Documentation: SpringDoc (Swagger UI)

## 시작 가이드

### 1. 사전 요구사항
* Java 17 이상
* (선택) Python AI 서버가 http://localhost:8000 에서 실행 중이어야 AI 기능을 정상적으로 테스트할 수 있습니다.

### 2. 설정 파일 (application.properties)
기본적으로 H2 인메모리 DB와 로컬 Python 서버를 사용하도록 설정되어 있습니다.

# AI Python Server URL
ai.python.server.url=http://localhost:8000

# H2 Database
spring.datasource.url=jdbc:h2:mem:testdb

### 3. 실행 방법
# Windows
./gradlew.bat bootRun

# Mac/Linux
./gradlew bootRun

## API 명세

서버가 실행된 후 아래 주소에서 API 문서를 확인할 수 있습니다.
* Swagger UI: http://localhost:8080/swagger-ui/index.html

### 주요 엔드포인트

| 구분 | Method | URI | 설명 |
| :--- | :---: | :--- | :--- |
| 실시간 통신 | WS | /ws-kiosk | WebSocket 연결 엔드포인트 |
| 메뉴 조회 | GET | /api/menu | 전체 또는 카테고리별 메뉴 목록 조회 |
| 주문 생성 | POST | /api/orders | 최종 주문 접수 (결제 완료 시) |

### WebSocket 메시지 흐름

1. Client -> Server (/pub/kiosk/message)
    {
      "userText": "치즈버거 하나 주세요",
      "currentState": "SELECT_BURGER"
    }

2. Server -> Client (/sub/kiosk/response)
    {
      "newState": "SELECT_DRINK",
      "spokenResponse": "치즈버거를 담았습니다. 음료는 무엇으로 하시겠어요?",
      "updatedCart": { "1": 1 }
    }

## 프로젝트 구조

src/main/java/com/slowkiosk/spring
├── config          # WebSocket, WebClient, CORS 설정
├── controller      # REST API 및 WebSocket 컨트롤러
├── dto             # 데이터 전송 객체 (AI 요청/응답 포함)
├── entity          # JPA 엔티티 (Menu, Order)
├── repository      # DB 레포지토리
├── service         # 비즈니스 로직 (Orchestration, Cart, AI)
└── listener        # WebSocket 연결/해제 이벤트 처리

## 주요 기능 테스트

1. 데이터 초기화: 서버 시작 시 DataInitializer가 메뉴 데이터(버거, 음료 등)를 H2 DB에 자동으로 생성합니다.
2. AI 연동: AiPythonService가 사용자의 발화와 현재 메뉴 및 장바구니 상태를 Python 서버로 전송하여 의도를 분석합니다.
3. 장바구니: 로그인 없이 WebSocket 세션 ID를 기반으로 메모리상에서 장바구니를 관리합니다.
