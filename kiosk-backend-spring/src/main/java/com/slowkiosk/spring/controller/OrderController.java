package com.slowkiosk.spring.controller;

import com.slowkiosk.spring.dto.OrderDto;
import com.slowkiosk.spring.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성 (POST /api/orders)
     * @param request (e.g., {"items": [{"menuId": 1, "quantity": 2}]})
     * @return 생성된 주문 정보 (OrderDto.Response)
     */
    @PostMapping
    public ResponseEntity<OrderDto.Response> createOrder(@RequestBody OrderDto.CreateRequest request) {
        OrderDto.Response response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}