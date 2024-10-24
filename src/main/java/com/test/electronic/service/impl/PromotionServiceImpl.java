package com.test.electronic.service.impl;

import com.test.electronic.exception.CategoryNotFoundException;
import com.test.electronic.exception.NotFoundException;
import com.test.electronic.mapper.PromotionMapper;
import com.test.electronic.model.dto.request.PromotionRequest;
import com.test.electronic.model.dto.response.PromotionResponse;
import com.test.electronic.model.entity.Category;
import com.test.electronic.model.entity.Product;
import com.test.electronic.model.entity.Promotion;
import com.test.electronic.model.enums.PromotionStatus;
import com.test.electronic.repository.CategoryRepository;
import com.test.electronic.repository.ProductRepository;
import com.test.electronic.repository.PromotionRepository;
import com.test.electronic.service.PromotionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Data
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    public PromotionResponse createPromotion(PromotionRequest request) {
        Promotion promotion = promotionMapper.toEntity(request);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        promotion.setCategory(category);

        String rawDiscountPrice = request.getDiscountPrice().toString();
        BigDecimal discountPrice = new BigDecimal(rawDiscountPrice.replace("%", ""));
        promotion.setDiscountPrice(discountPrice);

        promotion.updateStatus();
        promotion = promotionRepository.save(promotion);

        if (promotion.getStatus() == PromotionStatus.ACTIVE) {
            applyDiscountToCategoryProducts(category, promotion.getDiscountPrice());
        }

        return promotionMapper.toResponse(promotion);
    }


    private void applyDiscountToCategoryProducts(Category category, BigDecimal discountPrice) {
        List<Product> products = productRepository.findByCategory(category);

        for (Product product : products) {
            List<Promotion> promotions = promotionRepository.findByCategory(category);

            for (Promotion promotion : promotions) {
                if (promotion.getStatus() == PromotionStatus.ACTIVE) {
                    product.applyDiscount(promotion.getDiscountPrice());
                    productRepository.save(product);
                }
            }
        }
    }


    @Override
    public PromotionResponse getPromotionById(Long id) {
        Promotion promotion = checkIfExist(id);
        return promotionMapper.toResponse(promotion);
    }

    @Override
    public List<PromotionResponse> getAll() {
        List<Promotion> promotions = promotionRepository.findAll();
        return promotions.stream()
                .map(promotionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updatePromotionById(Long id, PromotionRequest request) {
        Promotion promotion = checkIfExist(id);
        promotionMapper.updateEntity(request, promotion);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        promotion.setCategory(category);
        promotionRepository.save(promotion);
    }

    @Override
    public void deletePromotionById(Long id) {
        promotionRepository.deleteById(id);
    }

    private Promotion checkIfExist(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Promotion not found"));
    }
}



