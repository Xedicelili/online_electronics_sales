package com.test.electronic.repository;

import com.test.electronic.model.entity.Comparison;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComparisonRepository extends JpaRepository<Comparison,Long> {
}
