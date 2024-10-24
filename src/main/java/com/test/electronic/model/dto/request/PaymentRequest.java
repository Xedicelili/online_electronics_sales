package com.test.electronic.model.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PaymentRequest {
    private CardDetailsRequest cardDetailsRequest;



    }


//    public PaymentRequest(Long orderId, BigDecimal amount, String currency, CardDetails cardDetails) {
//
//        this.orderId = orderId;
//        this.amount = amount;
//        this.currency = currency;
//        this.cardDetails = cardDetails;
//  private Long orderId;
//    private BigDecimal amount;
//    private String currency;
//    }


