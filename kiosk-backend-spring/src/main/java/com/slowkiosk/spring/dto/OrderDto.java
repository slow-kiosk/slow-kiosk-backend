package com.slowkiosk.spring.dto;

import com.slowkiosk.spring.entity.Order;
import com.slowkiosk.spring.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {

    // === 요청(Request) DTO ===

    @Getter
    @Setter
    public static class CreateRequest {
        // e.g., [{"menuId": 1, "quantity": 2}, {"menuId": 3, "quantity": 1}]
        private List<OrderItemRequest> items;
    }

    @Getter
    @Setter
    public static class OrderItemRequest {
        private Long menuId;
        private int quantity;
    }

    // === 응답(Response) DTO ===

    @Getter
    @Builder
    public static class Response {
        private Long orderId;
        private LocalDateTime orderTime;
        private double totalPrice;
        private List<OrderItemResponse> items;

        // Order 엔티티를 OrderDto.Response로 변환
        public static Response fromEntity(Order order) {
            return Response.builder()
                    .orderId(order.getId())
                    .orderTime(order.getOrderTime())
                    .totalPrice(order.getTotalPrice())
                    .items(order.getItems().stream()
                            .map(OrderItemResponse::fromEntity)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class OrderItemResponse {
        private Long menuId;
        private String menuName;
        private int quantity;
        private double priceAtOrder;

        // OrderItem 엔티티를 OrderItemResponse DTO로 변환
        public static OrderItemResponse fromEntity(OrderItem orderItem) {
            return OrderItemResponse.builder()
                    .menuId(orderItem.getMenu().getId())
                    .menuName(orderItem.getMenu().getName())
                    .quantity(orderItem.getQuantity())
                    .priceAtOrder(orderItem.getPriceAtOrder())
                    .build();
        }
    }
}