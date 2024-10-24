package com.test.electronic.controller;

import com.test.electronic.mapper.ComparisonMapper;
import com.test.electronic.mapper.ProductMapper;
import com.test.electronic.model.dto.request.ComparisonRequest;
import com.test.electronic.model.dto.response.ComparisonResponse;
import com.test.electronic.model.dto.response.ProductResponse;
import com.test.electronic.model.entity.Comparison;
import com.test.electronic.service.ComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comparison")
@RequiredArgsConstructor
public class ComparisonController {

    private final ComparisonService comparisonService;
    private final ComparisonMapper comparisonMapper;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<ComparisonResponse> createComparison(@RequestBody ComparisonRequest request) {
        Comparison comparison = comparisonService.createComparison(request);
        ComparisonResponse response = comparisonMapper.toComparisonResponse(comparison);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @RequestParam String categoryName) {
        List<ProductResponse> responses = comparisonService.getProductsByCategory(categoryName);
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/all")
    public List<Comparison> getAllComparisons() {
        return comparisonService.getAllComparisons();
    }

}