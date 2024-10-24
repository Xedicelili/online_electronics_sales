package com.test.electronic.controller;

import com.test.electronic.model.dto.request.CategoryRequest;
import com.test.electronic.model.dto.response.CategoryResponse;
import com.test.electronic.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponse add(@RequestBody CategoryRequest request) {
        return categoryService.add(request);
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id) {
        return categoryService.getById(id);
    }


    @GetMapping
    public List<CategoryResponse> getAll() {
        return categoryService.getAll();
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id, @RequestBody CategoryRequest request) {
        return categoryService.update(id, request);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  delete(@PathVariable Long id) {
      categoryService.delete(id);    }
}

