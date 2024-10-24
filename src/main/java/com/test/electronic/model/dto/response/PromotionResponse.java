package com.test.electronic.model.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PromotionResponse {

        private String title;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal discountPrice;
        private String categoryName;

}
