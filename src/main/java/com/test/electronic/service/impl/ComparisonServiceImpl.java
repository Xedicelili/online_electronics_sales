package com.test.electronic.service.impl;

import com.test.electronic.exception.ProductNotFoundException;
import com.test.electronic.mapper.ProductMapper;
import com.test.electronic.model.dto.request.ComparisonRequest;
import com.test.electronic.model.dto.response.ProductResponse;
import com.test.electronic.model.entity.Comparison;
import com.test.electronic.model.entity.Product;
import com.test.electronic.repository.ComparisonRepository;
import com.test.electronic.service.ComparisonService;
import com.test.electronic.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComparisonServiceImpl implements ComparisonService {

    private final ProductService productService;
    private final ComparisonRepository comparisonRepository;
    private final ProductMapper productMapper;

    @Override
    public Comparison createComparison(ComparisonRequest request) {
        List<String> productNames = request.getProducts().stream()
                .map(ComparisonRequest.ProductDetail::getName)
                .collect(Collectors.toList());

        List<Product> products = productService.findProductsByNames(productNames);

        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found with the provided names");
        }

        Comparison comparison = new Comparison();
        comparison.setProducts(products);

        return comparisonRepository.save(comparison);
    }

    public List<ProductResponse> getProductsByCategory(String categoryName) {
        List<Product> products = productService.findProductsByCategoryName(categoryName);
        return products.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    public List<Comparison> getAllComparisons() {
        return comparisonRepository.findAll();
    }

}