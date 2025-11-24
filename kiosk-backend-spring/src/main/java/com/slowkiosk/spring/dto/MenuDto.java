package com.slowkiosk.spring.dto;

import com.slowkiosk.spring.entity.Menu;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class MenuDto {

    @Getter
    @Setter
    public static class CreateRequest {
        private String name;
        private double price;
        private String description;
        private String imageUrl;
        private String category;
        private String nutrition; // 추가
        private String tags;      // 추가
    }

    @Getter
    @Setter
    public static class UpdateRequest {
        private String name;
        private double price;
        private String description;
        private String imageUrl;
        private String category;
        private String nutrition; // 추가
        private String tags;      // 추가
    }

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private double price;
        private String description;
        private String imageUrl;
        private String category;
        private String nutrition; // 추가
        private String tags;      // 추가

        public static Response fromEntity(Menu menu) {
            return Response.builder()
                    .id(menu.getId())
                    .name(menu.getName())
                    .price(menu.getPrice())
                    .description(menu.getDescription())
                    .imageUrl(menu.getImageUrl())
                    .category(menu.getCategory())
                    .nutrition(menu.getNutrition()) // 매핑 추가
                    .tags(menu.getTags())           // 매핑 추가
                    .build();
        }
    }
}