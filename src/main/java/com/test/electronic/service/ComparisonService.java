package com.test.electronic.service;

import com.test.electronic.model.dto.request.ComparisonRequest;
import com.test.electronic.model.dto.response.ProductResponse;
import com.test.electronic.model.entity.Comparison;

import java.util.List;

public interface ComparisonService {

    Comparison createComparison(ComparisonRequest request);
    List<ProductResponse> getProductsByCategory(String categoryName);
    List<Comparison> getAllComparisons();


}
