package com.test.electronic.repository;

import com.test.electronic.model.entity.Category;
import com.test.electronic.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findByCategory(Category category);

    List<Product> findProductsByCategoryId(Long categoryId);

    List<Product> findByNameIn(List<String> names);


}
