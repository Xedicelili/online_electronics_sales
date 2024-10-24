package com.test.electronic.service.impl;

import com.test.electronic.exception.CategoryNotFoundException;
import com.test.electronic.exception.NotFoundException;
import com.test.electronic.exception.ProductValidationException;
import com.test.electronic.mapper.ProductMapper;
import com.test.electronic.model.dto.request.ProductRequest;
import com.test.electronic.model.dto.response.ProductResponse;
import com.test.electronic.model.entity.Category;
import com.test.electronic.model.entity.Product;
import com.test.electronic.model.entity.Promotion;
import com.test.electronic.model.enums.PromotionStatus;
import com.test.electronic.repository.CategoryRepository;
import com.test.electronic.repository.ProductRepository;
import com.test.electronic.repository.PromotionRepository;
import com.test.electronic.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final PromotionRepository promotionRepository;

    @Override

    public Product createProduct(ProductRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ProductValidationException("Product name cannot be empty");
        }

        Product product = productMapper.toEntity(request);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        product.setCategory(category);

        List<Promotion> promotions = promotionRepository.findByCategoryAndStatus(category, PromotionStatus.ACTIVE);

        for (Promotion promotion : promotions) {
            product.applyDiscount(promotion.getDiscountPrice());
        }

        return productRepository.save(product);
    }


    @Override
    public ProductResponse getProductById(Long id) {
        Product product = checkIfExist(id);
        return productMapper.toResponse(product);
    }

    @Override
    public List<Product> findProductsByCategoryName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        return productRepository.findProductsByCategoryId(category.getId());
    }

    @Override
    public void updateProductById(Long id, ProductRequest request) {
        Product product = checkIfExist(id);
        productMapper.updateEntity(request, product);
        productRepository.save(product);
    }


    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    private Product checkIfExist(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }


    @Override
    public List<Product> findProductsByNames(List<String> productNames) {
        return productRepository.findByNameIn(productNames);
    }


    @Override
    public List<ProductResponse> findProductsByCategory_Name(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        List<Product> products = productRepository.findProductsByCategoryId(category.getId());

        return products.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }


}

