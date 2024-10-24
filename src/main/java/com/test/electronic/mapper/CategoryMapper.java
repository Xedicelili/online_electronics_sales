package com.test.electronic.mapper;

import com.test.electronic.model.dto.request.CategoryRequest;
import com.test.electronic.model.dto.response.CategoryResponse;
import com.test.electronic.model.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category category);

}
