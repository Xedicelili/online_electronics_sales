package com.test.electronic.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
    public class OrderRequest {
        private Long userId;

    private List< ProductOrder> products;

    private PaymentRequest paymentRequest;

    @Data
        public static class ProductOrder {
            private Long productId;
            private int quantity;
        }


}
