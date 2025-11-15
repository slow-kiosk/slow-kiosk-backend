package com.slowkiosk.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders") // 'ORDER'는 SQL 예약어이므로 'orders'로 지정
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime orderTime;

    private double totalPrice;

    // (중요) Order(1)가 OrderItem(N)을 관리합니다.
    // CascadeType.ALL: Order를 저장/삭제할 때, OrderItem도 함께 저장/삭제됩니다.
    // orphanRemoval = true: Order의 items 리스트에서 OrderItem을 제거하면 DB에서도 삭제됩니다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    // == 연관관계 편의 메서드 == //
    // Order에 OrderItem을 추가할 때, 양방향 관계를 설정합니다.
    public void addOrderItem(OrderItem orderItem) {
        this.items.add(orderItem);
        orderItem.setOrder(this);
    }

    // == 생성 편의 메서드 == //
    public static Order createOrder(List<OrderItem> orderItems) {
        Order order = new Order();
        order.setOrderTime(LocalDateTime.now());
        double total = 0;

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
            total += orderItem.getTotalPrice();
        }
        order.setTotalPrice(total);
        return order;
    }
}