package com.test.electronic.model.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private String name;
    private String color;
    private BigDecimal price;
    private String warrantyPeriod;
    private String categoryName;
    private BigDecimal discountedPrice;






}
