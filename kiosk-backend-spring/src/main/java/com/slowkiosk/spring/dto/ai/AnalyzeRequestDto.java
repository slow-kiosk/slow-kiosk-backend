package com.slowkiosk.spring.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        private List<String> tags;

        // private String nutrition; // [삭제] 혹은 아래와 같이 변경

        // [수정] Python의 MenuItem 모델 필드명과 정확히 일치시켜야 함 ("nutrition_summary_ko")
        @JsonProperty("nutrition_summary_ko") // Jackson 라이브러리 어노테이션 추가 필요
        private String nutrition;

        // 또는 변수명 자체를 변경
        // private String nutrition_summary_ko;

        private String description;
        private String customizable_ko;
    }
}