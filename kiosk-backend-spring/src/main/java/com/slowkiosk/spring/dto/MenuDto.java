package com.slowkiosk.spring.dto;

import com.slowkiosk.spring.entity.Menu;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class MenuDto {

    // === 요청(Request) DTO ===

    @Getter
    @Setter // Controller에서 JSON을 객체로 변환(Deserialize)할 때 필요
    public static class CreateRequest {
        private String name;
        private double price;
        private String description;
        private String imageUrl;
        private String category;
    }

    @Getter
    @Setter
    public static class UpdateRequest {
        private String name;
        private double price;
        private String description;
        private String imageUrl;
        private String category;
    }

    // === 응답(Response) DTO ===

    @Getter
    @Builder // Service에서 Entity -> DTO 변환 시 편리
    public static class Response {
        private Long id;
        private String name;
        private double price;
        private String description;
        private String imageUrl;
        private String category;

        // Service 레이어에서 Entity를 DTO로 변환하는 정적 메서드
        public static Response fromEntity(Menu menu) {
            return Response.builder()
                    .id(menu.getId())
                    .name(menu.getName())
                    .price(menu.getPrice())
                    .description(menu.getDescription())
                    .imageUrl(menu.getImageUrl())
                    .category(menu.getCategory())
                    .build();
        }
    }
}