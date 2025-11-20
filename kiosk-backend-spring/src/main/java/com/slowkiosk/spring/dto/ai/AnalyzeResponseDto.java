package com.slowkiosk.spring.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AnalyzeResponseDto {

    @JsonProperty("assistant_text")
    private String assistantText; // TTS로 읽어줄 멘트

    @JsonProperty("next_scene")
    private String nextScene;     // 다음 화면 상태

    @JsonProperty("should_finish")
    private boolean shouldFinish; // 주문 종료 여부

    private List<KioskAction> actions; // AI가 판단한 행동 목록

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class KioskAction {
        private String type;   // ADD_ITEM, REMOVE_ITEM, NONE 등
        private String menuId; // 대상 메뉴 ID
        private Integer qty;   // 수량
        // customize 등은 추후 확장
    }
}