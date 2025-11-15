package com.slowkiosk.spring.service;

import com.slowkiosk.spring.dto.OrderDto;
import com.slowkiosk.spring.entity.Menu;
import com.slowkiosk.spring.entity.Order;
import com.slowkiosk.spring.entity.OrderItem;
import com.slowkiosk.spring.repository.MenuRepository;
import com.slowkiosk.spring.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository; // (실제 MenuRepository 주입)

    /**
     * 주문 생성
     * 이 메서드 전체가 하나의 트랜잭션으로 묶입니다.
     * 중간에 MenuRepository에서 예외가 터지면, 모든 DB 작업이 롤백됩니다.
     */
    public OrderDto.Response createOrder(OrderDto.CreateRequest request) {

        List<OrderItem> orderItems = new ArrayList<>();

        // 1. 요청받은 항목(DTO)들을 OrderItem 엔티티로 변환
        for (OrderDto.OrderItemRequest itemRequest : request.getItems()) {

            // 1a. (실제 DB 조회) MenuRepository를 사용해 메뉴 정보를 가져옵니다.
            Menu menu = menuRepository.findById(itemRequest.getMenuId())
                    .orElseThrow(() -> new EntityNotFoundException("Menu not found: " + itemRequest.getMenuId()));

            // 1b. OrderItem 생성 (이때 실제 메뉴 가격이 저장됨)
            OrderItem orderItem = OrderItem.createOrderItem(menu, itemRequest.getQuantity());
            orderItems.add(orderItem);
        }

        // 2. OrderItem 리스트를 기반으로 Order(주문서) 생성
        Order order = Order.createOrder(orderItems);

        // 3. Order 저장 (Cascade 설정으로 OrderItem들도 함께 저장됨)
        Order savedOrder = orderRepository.save(order);

        // 4. DTO로 변환하여 반환
        return OrderDto.Response.fromEntity(savedOrder);
    }
}