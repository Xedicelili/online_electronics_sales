package com.test.electronic.controller;


import com.test.electronic.model.dto.response.PaymentResponse;
import com.test.electronic.model.entity.User;
import com.test.electronic.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
    @RequestMapping("/payments")
    public class PaymentController {


        private final PaymentService paymentService;

            @GetMapping
            public Page<PaymentResponse> getPaymentsByUser(
                    @AuthenticationPrincipal User user,
                    Pageable pageable) {
                return paymentService.getPaymentsByUser(user, pageable);
            }

        }
