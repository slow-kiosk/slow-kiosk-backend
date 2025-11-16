package com.slowkiosk.spring.service;

import com.slowkiosk.spring.dto.OrderDto;
import com.slowkiosk.spring.entity.Menu;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
// (중요) 이 빈(Bean)의 범위를 '웹 세션'으로 지정합니다.
// WebSocket 연결 시 생성되어, 연결이 끊어질 때까지 동일한 인스턴스가 사용됩니다.
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
public class CartService {

    // Key: Menu ID, Value: Quantity
    private final Map<Long, Integer> cartItems = new HashMap<>();

    // 장바구니 총액 계산 등을 위해 MenuRepository가 필요할 수 있습니다.
    // (지금은 OrchestrationService가 처리하지만, CartService가 직접 할 수도 있습니다)
    // private final MenuRepository menuRepository;
    // public CartService(MenuRepository menuRepository) {
    //     this.menuRepository = menuRepository;
    // }

    /**
     * 장바구니에 메뉴 추가 (또는 수량 증가)
     */
    public void addItem(Menu menu, int quantity) {
        cartItems.put(menu.getId(), cartItems.getOrDefault(menu.getId(), 0) + quantity);
    }

    /**
     * 장바구니 비우기
     */
    public void clearCart() {
        cartItems.clear();
    }

    /**
     * OrderService에 전달할 OrderDto.CreateRequest DTO를 생성합니다.
     */
    public OrderDto.CreateRequest createOrderRequestDto() {
        List<OrderDto.OrderItemRequest> orderItems = this.cartItems.entrySet().stream()
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

    // TODO: React에 현재 장바구니 UI를 그려주기 위한 DTO 반환 메서드 (e.g., getCartDto())
}