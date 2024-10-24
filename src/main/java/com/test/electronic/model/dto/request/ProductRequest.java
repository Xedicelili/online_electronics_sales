package com.test.electronic.model.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String name;
    private String color;
    private BigDecimal price;
    private String warrantyPeriod;
    private Integer stockQuantity;
    private Long categoryId;


}


