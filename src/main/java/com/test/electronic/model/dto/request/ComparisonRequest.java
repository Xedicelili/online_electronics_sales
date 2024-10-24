package com.test.electronic.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ComparisonRequest {
    private List<ProductDetail> products;

    @Data
    public static class ProductDetail {
        private String name;
    }
}
