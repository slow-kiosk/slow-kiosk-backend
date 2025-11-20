package com.slowkiosk.spring.dto.ai;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AnalyzeRequestDto {
    private String text;      // 사용자 발화
    private String scene;     // 현재 화면 (currentState)
    private AiCartDto cart;   // 현재 장바구니 상태
    private List<AiMenuDto> menu; // 현재 선택 가능한 메뉴 리스트

    @Getter
    @Setter
    @Builder
    public static class AiCartDto {
        private List<AiCartItemDto> items;
    }

    @Getter
    @Setter
    @Builder
    public static class AiCartItemDto {
        private String menuId;
        private int qty;
    }

    @Getter
    @Setter
    @Builder
    public static class AiMenuDto {
        private String menuId;
        private String name;
        private String category;
        private int price;
        // tags 등은 필요하다면 추가
    }
}