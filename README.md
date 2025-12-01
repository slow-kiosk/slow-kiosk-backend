<div align="center">

# Slow Kiosk Backend

### AI-Powered Voice Kiosk Orchestrator

**Slow Kiosk**의 두뇌 역할을 하는 Spring Boot 백엔드 서버입니다.  
사용자의 음성 입력을 실시간으로 수신하여 대화의 문맥(Context)을 분석하고,  
Python AI 서버와 프론트엔드 사이를 중재하는 **오케스트레이터(Orchestrator)** 역할을 수행합니다.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![WebSocket](https://img.shields.io/badge/WebSocket-STOMP-blue.svg)](https://stomp.github.io/)
[![H2 Database](https://img.shields.io/badge/Database-H2-blue.svg)](https://www.h2database.com/)

</div>

---


## 시스템 아키텍처

이 프로젝트는 **Hybrid AI Architecture**를 채택하여 반응 속도와 연산 효율성을 최적화했습니다.

```text
┌─────────────────┐    WebSocket (STOMP)     ┌────────────────────────┐    HTTP (REST)     ┌────────────────────┐
│                 │ ◄──────────────────────► │                        │ ◄────────────────► │                    │
│  React Client   │     /ws-kiosk            │  Spring Boot Server    │    /analyze        │  Python AI Server  │
│  (Frontend/UI)  │                          │  (Backend/Orchestrator)│                    │  (LLM Engine)      │
│                 │                          │                        │                    │                    │
└─────────────────┘                          └────────────────────────┘                    └────────────────────┘
      
    음성 인식 (STT)                             대화 세션/장바구니 관리                        사용자 의도 파악
    화면 렌더링                                 AI에 보낼 문맥(Context) 조립                   답변/행동 생성
    음성 합성 (TTS)                             메뉴/주문 데이터 관리 (DB)
```

<div align="center">

### 데이터 흐름

사용자 음성 → STT → WebSocket → Context 조립 → AI 분석 → 응답 생성 → TTS → 사용자

</div>

---

## 주요 기능

### 1. 실시간 대화 오케스트레이션 (WebSocket)

<table>
<tr>
<td width="50%">

#### 양방향 통신
STOMP 프로토콜을 사용하여 프론트엔드와 실시간으로 메시지를 주고받습니다.

</td>
<td width="50%">

#### 세션 관리
WebSocket 세션 ID를 기반으로 로그인 없이도 사용자별 독립적인 장바구니와 대화 흐름을 유지합니다.

</td>
</tr>
</table>

---

### 2. Context-Aware AI 연동

단순히 사용자의 말만 전달하는 것이 아니라, **현재 상황(Context)**을 함께 AI 서버로 전송합니다.

<div align="center">

| Context Type | 설명 | 예시 |
|:------------:|------|------|
| **Cart** | 현재 장바구니에 담긴 메뉴와 수량 | `불고기버거 x2, 콜라 x1` |
| **Menu** | DB의 메뉴 영양 성분, 알레르기 정보, 추천 태그 | `"어르신추천", "소화잘되는"` |
| **History** | 이전 대화 내역 | `[세트 선택 완료] → [음료 선택 중]` |

</div>

---

### 3. 데이터 자동 초기화 (DataInitializer)

> 서버 실행 시 H2 인메모리 DB에 키오스크용 메뉴 데이터를 자동으로 적재합니다.

- 노년층 친화적 메뉴 태그 포함
  - `부드러운` `속편한` `맵지않은`
- 자동 생성되는 데이터
  - 버거 메뉴
  - 사이드 메뉴
  - 음료 메뉴

---

## 기술 스택

<div align="center">

### Core Technologies

| Category | Technology | Version |
|:--------:|:----------:|:-------:|
| **Language** | Java | 17 |
| **Framework** | Spring Boot | 3.5.7 |
| **WebSocket** | Spring WebSocket (STOMP) | - |
| **HTTP Client** | Spring WebFlux (WebClient) | - |
| **Database** | H2 Database (In-Memory) | - |
| **ORM** | Spring Data JPA | - |
| **Build Tool** | Gradle | - |

</div>

---

## API 명세

### 1. WebSocket (실시간 통신)

<div align="center">

#### Connection Info

| 항목 | 값 |
|:----:|:--:|
| **Endpoint** | `/ws-kiosk` |
| **Protocol** | STOMP over SockJS |
| **Connection** | `ws://localhost:8080/ws-kiosk` |

</div>

#### 메시지 송수신

**Client → Server**
```javascript
// Destination: /pub/kiosk/message
{
  "userText": "불고기 버거 줘",
  "currentState": "HOME"
}
```

**Server → Client**
```javascript
// Destination: /sub/kiosk/response
{
  "newState": "SELECT_SET",
  "spokenResponse": "세트로 드릴까요?",
  "cartItems": [...],
  "suggestedMenus": [...]
}
```

---

### 2. REST API (데이터 조회/주문)

<div align="center">

| Method | Endpoint | Description | Response |
|:------:|:---------|:------------|:---------|
| `GET` | `/api/menu` | 전체 메뉴 목록 조회 | `List<Menu>` |
| `GET` | `/api/menu/{id}` | 특정 메뉴 상세 조회 | `Menu` |
| `POST` | `/api/orders` | 최종 주문 생성 | `Order` |

</div>

#### 📖 API 문서 (Swagger)

서버 실행 후 아래 주소에서 **대화형 API 문서**를 확인할 수 있습니다.

<div align="center">

### 🔗 [Swagger UI 바로가기](http://localhost:8080/swagger-ui/index.html)

`http://localhost:8080/swagger-ui/index.html`

</div>

---

## 설치 및 실행

### ⚙️ 1. 사전 요구사항

<table>
<tr>
<td width="50%">

#### ✅ 필수 설치
- **Java 17 이상**
- **Gradle** (또는 Wrapper 사용)

</td>
<td width="50%">

#### ⚠️ 중요 사항
- **Python AI 서버** 필수
  - `http://localhost:8000` 에서 실행 중이어야 함
  - 미실행 시 기본 응답 모드 또는 에러 발생

</td>
</tr>
</table>

---

### 2. 프로젝트 설정

`src/main/resources/application.properties`

```properties
# Python AI 서버 주소
ai.python.server.url=http://localhost:8000

# H2 데이터베이스 설정 (메모리 모드)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop

# H2 콘솔 활성화 (개발 환경)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---

### 3. 실행 명령어

<table>
<tr>
<td width="50%">

#### 🍎 Mac / Linux

```bash
# 실행 권한 부여
chmod +x gradlew

# 서버 시작
./gradlew bootRun
```

</td>
<td width="50%">

#### 🪟 Windows

```bash
# 서버 시작
gradlew.bat bootRun
```

</td>
</tr>
</table>

---

### 4. 실행 확인

서버가 정상적으로 실행되면 다음 주소들이 활성화됩니다:

<div align="center">

| Service | URL | Description |
|:-------:|:----|:------------|
| **Main Server** | `http://localhost:8080` | 메인 서버 |
| **WebSocket** | `ws://localhost:8080/ws-kiosk` | 실시간 통신 |
| **Swagger UI** | `http://localhost:8080/swagger-ui/index.html` | API 문서 |
| **H2 Console** | `http://localhost:8080/h2-console` | DB 관리 콘솔 |

</div>

---

## 프로젝트 구조

```
src/main/java/com/slowkiosk/spring/
│
├── 📁 config/                        # 설정 클래스
│   ├── DataInitializer.java       # 초기 메뉴 데이터 자동 생성
│   ├── WebClientConfig.java       # Python 서버 연동 설정
│   └── WebSocketConfig.java       # STOMP/SockJS 설정
│
├── 📁 controller/                    # API 컨트롤러
│   ├── KioskSocketController.java # WebSocket 메시지 핸들러
│   └── MenuController.java        # 메뉴 조회 REST API
│
├── 📁 service/                       # 비즈니스 로직
│   ├── OrchestrationService.java  # [핵심] 대화 흐름 및 상태 관리
│   ├── AiPythonService.java       # Python AI 서버 API 호출
│   └── CartService.java           # 장바구니 메모리 관리
│
├── 📁 dto/                           # 데이터 전송 객체
│   └── 📁 ai/                        # AI 서버 통신용 DTO
│       ├── AiRequest.java            # AI 요청 DTO
│       └── AiResponse.java           # AI 응답 DTO
│
└── 📁 entity/                        # JPA 엔티티
    ├── Menu.java                  # 메뉴 엔티티
    ├── Order.java                 # 주문 엔티티
    └── OrderItem.java             # 주문 항목 엔티티
```

---

<div align="center">
</div>
