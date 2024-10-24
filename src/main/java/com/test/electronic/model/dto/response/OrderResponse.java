package com.test.electronic.model.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

    @Data
    public class OrderResponse {
        private BigDecimal totalPrice;
        private int productQuantity;
        private LocalDate orderDate;
        private String status;
        private UserResponse user;
        private List<ProductResponse> products;

        @Data
        public static class ProductResponse {
            private String name;
            private BigDecimal price;
            private int quantity;
        }

        @Data
        public static class UserResponse {
            private Long id;
            private String email;
            private String name;
        }

}
