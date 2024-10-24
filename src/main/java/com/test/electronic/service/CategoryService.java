package com.test.electronic.service;

import com.test.electronic.model.dto.request.CategoryRequest;
import com.test.electronic.model.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse add(CategoryRequest request);
    CategoryResponse getById(Long id);
    List<CategoryResponse> getAll();
    CategoryResponse update(Long id, CategoryRequest request);
    void delete(Long id);
}
