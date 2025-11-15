package com.slowkiosk.spring.repository;

import com.slowkiosk.spring.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // OrderItemRepository는 Order의 Cascade 설정 때문에 만들지 않아도 됩니다.
}