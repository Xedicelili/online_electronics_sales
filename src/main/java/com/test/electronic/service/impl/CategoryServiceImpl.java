package com.test.electronic.service.impl;

import com.test.electronic.exception.NotFoundException;
import com.test.electronic.mapper.CategoryMapper;
import com.test.electronic.model.dto.request.CategoryRequest;
import com.test.electronic.model.dto.response.CategoryResponse;
import com.test.electronic.model.entity.Category;
import com.test.electronic.repository.CategoryRepository;
import com.test.electronic.service.CategoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Data

public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponse add(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }


    @Override
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        return categoryMapper.toResponse(category);

    }


    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toResponse).collect(Collectors.toList());

    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        category.setName(request.getName());
        category = categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);

    }
}
