package com.test.electronic.service;

import com.test.electronic.model.dto.request.PromotionRequest;
import com.test.electronic.model.dto.response.PromotionResponse;

import java.util.List;

public interface PromotionService {
    PromotionResponse createPromotion(PromotionRequest request);

    List<PromotionResponse> getAll();

    PromotionResponse getPromotionById(Long id);

    void updatePromotionById(Long id, PromotionRequest request);

    void deletePromotionById(Long id);
}


