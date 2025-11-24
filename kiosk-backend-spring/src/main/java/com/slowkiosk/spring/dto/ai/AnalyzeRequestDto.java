package com.slowkiosk.spring.dto.ai;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Python AI 서버로 보내는 요청 객체
 * (Python 서버의 AnalyzeRequest 모델과 1:1 매핑)
 */
@Getter
@Setter
@Builder
@ToString
public class AnalyzeRequestDto {

    private String text;      // 사용자 발화 (STT 완료된 텍스트)
    private String scene;     // 현재 화면 상태 (e.g., "SELECT_BURGER")
    private AiCartDto cart;   // 현재 장바구니 상태
    private List<AiMenuDto> menu; // 현재 주문 가능한 메뉴 목록

    // --- 내부 클래스 (Nested DTOs) ---

    @Getter
    @Setter
    @Builder
    @ToString
    public static class AiCartDto {
        private List<AiCartItemDto> items;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    public static class AiCartItemDto {
        private String menuId; // Python은 String ID 사용
        private int qty;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    public static class AiMenuDto {
        private String menuId;
        private String name;
        private String category;
        private int price;

        // [중요] Python 서버의 models.py와 필드명을 정확히 일치시켜야 합니다.
        private List<String> tags;          // 예: ["베스트", "매운맛"]
        private String ingredients_ko;      // 재료 및 설명 (Spring에서 합쳐서 보냄)
        private String customizable_ko;     // 커스터마이징 정보 (사용 안 하면 null)
    }
}