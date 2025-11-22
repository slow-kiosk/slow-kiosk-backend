package com.slowkiosk.spring.service;

import com.slowkiosk.spring.dto.OrderDto;
import com.slowkiosk.spring.entity.Menu;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
// @SessionScope 제거! (WebSocket과 호환되지 않음)
public class CartService {

    // 모든 사용자의 장바구니를 관리하는 저장소
    // Key: Session ID (String), Value: 사용자별 장바구니 (MenuId -> Qty)
    private final Map<String, Map<Long, Integer>> carts = new ConcurrentHashMap<>();

    /**
     * 특정 세션의 장바구니 가져오기 (없으면 생성)
     */
    public Map<Long, Integer> getCart(String sessionId) {
        return carts.computeIfAbsent(sessionId, k -> new HashMap<>());
    }

    /**
     * 장바구니에 메뉴 추가
     */
    // CartService.java 수정 제안
    public void addItem(String sessionId, Menu menu, int quantity) {
        Map<Long, Integer> userCart = getCart(sessionId);
        int currentQty = userCart.getOrDefault(menu.getId(), 0);
        int newQty = currentQty + quantity;

        if (newQty <= 0) {
            userCart.remove(menu.getId()); // 0개 이하면 장바구니에서 제거
        } else {
            userCart.put(menu.getId(), newQty);
        }
    }
    /**
     * 장바구니 비우기
     */
    public void clearCart(String sessionId) {
        carts.remove(sessionId);
    }

    /**
     * 주문 DTO 생성
     */
    public OrderDto.CreateRequest createOrderRequestDto(String sessionId) {
        Map<Long, Integer> userCart = getCart(sessionId);

        List<OrderDto.OrderItemRequest> orderItems = userCart.entrySet().stream()
                .map(entry -> {
                    OrderDto.OrderItemRequest item = new OrderDto.OrderItemRequest();
                    item.setMenuId(entry.getKey());
                    item.setQuantity(entry.getValue());
                    return item;
                })
                .collect(Collectors.toList());

        OrderDto.CreateRequest orderRequest = new OrderDto.CreateRequest();
        orderRequest.setItems(orderItems);
        return orderRequest;
    }
}