package com.test.electronic.repository;

import com.test.electronic.model.entity.Category;
import com.test.electronic.model.entity.Promotion;
import com.test.electronic.model.enums.PromotionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByCategory(Category category);
    List<Promotion> findByCategoryAndStatus(Category category, PromotionStatus status);
}

