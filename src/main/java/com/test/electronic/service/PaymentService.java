package com.test.electronic.service;

import com.test.electronic.model.dto.request.PaymentRequest;
import com.test.electronic.model.dto.response.PaymentResponse;
import com.test.electronic.model.entity.Payment;
import com.test.electronic.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    PaymentResponse processPayment(PaymentRequest paymentRequest, Long orderId);
    Page<PaymentResponse> getPaymentsByUser(User user, Pageable pageable);
    void savePayment(Payment payment); // Add this method


}


