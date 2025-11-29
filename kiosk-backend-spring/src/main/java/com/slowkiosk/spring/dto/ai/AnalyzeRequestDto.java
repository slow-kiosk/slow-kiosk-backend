package com.slowkiosk.spring.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
public class AnalyzeRequestDto {

    private String text;
    private String scene;
    private AiCartDto cart;
    private List<AiMenuDto> menu;

    // [추가] AI 서버로 전달할 대화 내역
    private List<Map<String, String>> history;

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

        @JsonProperty("ingredients_ko")
        private String ingredientsKo;

        @JsonProperty("nutrition_summary_ko")
        private String nutritionSummaryKo;

        @JsonProperty("allergy_warning_ko")
        private String allergyWarningKo;

        private String description;

        @JsonProperty("customizable_ko")
        private String customizableKo;
    }
}