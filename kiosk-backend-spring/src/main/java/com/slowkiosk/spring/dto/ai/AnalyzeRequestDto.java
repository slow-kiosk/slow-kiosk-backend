package com.slowkiosk.spring.dto.ai;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class AnalyzeRequestDto {

    private String text;
    private String scene;
    private AiCartDto cart;
    private List<AiMenuDto> menu;

    // ... (AiCartDto, AiCartItemDto는 기존과 동일) ...
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
        private String menuId;
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

        // [수정] 파이썬 서버와 명확하게 데이터를 나누기 위해 필드 분리
        private List<String> tags;
        private String ingredients_ko;      // 순수 재료 정보만 담음

        // [신규 추가]
        private String description;         // 메뉴 설명
        private String nutrition;           // 영양 성분

        private String customizable_ko;
    }
}