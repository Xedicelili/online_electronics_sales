package com.test.electronic.controller;

import com.test.electronic.model.dto.request.OrderRequest;
import com.test.electronic.model.dto.response.OrderResponse;
import com.test.electronic.model.entity.User;
import com.test.electronic.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor

public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.createOrder(request);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse orderResponse = orderService.getOrderById(id);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrders(@AuthenticationPrincipal User user, Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(pageable.getPageNumber() - 1, 10, pageable.getSort());
        Page<OrderResponse> orders = orderService.getOrdersByUser(user, adjustedPageable);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrderById(@PathVariable Long id, @RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.updateOrderById(id, request);
        return ResponseEntity.ok(orderResponse);
    }



}












