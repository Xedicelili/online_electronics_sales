package com.test.electronic.service;

import com.test.electronic.model.dto.request.OrderRequest;
import com.test.electronic.model.dto.response.OrderResponse;
import com.test.electronic.model.entity.User;
import com.test.electronic.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);
    void updateOrderStatus(Long orderId, OrderStatus status);
    OrderResponse getOrderById(Long id);
    Page<OrderResponse> getAllOrders(Pageable pageable);
    void deleteOrderById(Long id);
    OrderResponse updateOrderById(Long id, OrderRequest request);
    Page<OrderResponse> getOrdersByUser(User user, Pageable pageable);


//    void processOrder(Long orderId, PaymentRequest paymentRequest);



}