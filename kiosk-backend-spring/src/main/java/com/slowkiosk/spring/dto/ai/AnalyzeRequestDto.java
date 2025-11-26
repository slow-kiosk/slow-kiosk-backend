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

        // [기존 유지 및 매핑 명시] 재료 정보
        // Python의 ingredients_ko 필드와 매핑
        @JsonProperty("ingredients_ko")
        private String ingredientsKo;

        // [수정] 영양 정보 요약 (nutrition -> nutritionSummaryKo)
        // Python의 nutrition_summary_ko 필드와 매핑
        @JsonProperty("nutrition_summary_ko")
        private String nutritionSummaryKo;

        // [신규 추가] 알레르기 주의 문구
        // Python의 allergy_warning_ko 필드와 매핑
        @JsonProperty("allergy_warning_ko")
        private String allergyWarningKo;

        private String description;

        // [수정] 커스터마이즈 옵션 (변수명을 Java 관례에 맞게 변경하고 매핑)
        @JsonProperty("customizable_ko")
        private String customizableKo;
    }
}