package com.test.electronic.controller;

import com.test.electronic.model.dto.request.PromotionRequest;
import com.test.electronic.model.dto.response.PromotionResponse;
import com.test.electronic.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/promotions")

public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping
    public PromotionResponse createPromotion(@RequestBody PromotionRequest request) {
        return promotionService.createPromotion(request);
    }

    @GetMapping("/{id}")
    public PromotionResponse getPromotionById(@PathVariable Long id) {
        return promotionService.getPromotionById(id);
    }

    @GetMapping
    public List<PromotionResponse> getAll() {
        return promotionService.getAll();
    }

    @PutMapping("/{id}")
    public void updatePromotionById(@PathVariable Long id, @RequestBody PromotionRequest request) {
        promotionService.updatePromotionById(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePromotionById(@PathVariable Long id) {
        promotionService.deletePromotionById(id);
    }
}

