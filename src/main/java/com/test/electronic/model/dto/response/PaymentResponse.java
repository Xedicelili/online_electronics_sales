package com.test.electronic.model.dto.response;

import com.test.electronic.model.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
    public class PaymentResponse {

    private BigDecimal amount;
    private LocalDate paymentDate;
    private PaymentStatus paymentStatus;
    private String currency;
    private String companyBankAccount;
    private Long orderId;
    private CardDetailsResponse cardDetails;
    private String message;

        private boolean paymentSuccessful;


}
