package com.slowkiosk.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity; // 수량

    // (중요) 주문 시점의 가격 (나중에 메뉴 가격이 변경되어도 영향받지 않도록)
    private double priceAtOrder;

    // OrderItem(N)이 Menu(1)를 가리킵니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    // OrderItem(N)이 Order(1)에 속합니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // == 생성 편의 메서드 == //
    public static OrderItem createOrderItem(Menu menu, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setMenu(menu);
        orderItem.setQuantity(quantity);
        orderItem.setPriceAtOrder(menu.getPrice()); // 현재 메뉴 가격을 '주문 시점 가격'으로 기록
        return orderItem;
    }

    // == 조회 로직 == //
    public double getTotalPrice() {
        return this.priceAtOrder * this.quantity;
    }
}