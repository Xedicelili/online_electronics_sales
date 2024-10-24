package com.test.electronic.model.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PromotionRequest {

        private String title;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private Long categoryId;
        private BigDecimal discountPrice;

}


