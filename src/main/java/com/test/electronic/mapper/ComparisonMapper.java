package com.test.electronic.mapper;

import com.test.electronic.model.dto.response.ComparisonResponse;
import com.test.electronic.model.entity.Comparison;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ComparisonMapper {


        @Mapping(target = "products", source = "comparison.products")
        ComparisonResponse toComparisonResponse(Comparison comparison);

        // Optionally, if you have reverse mapping or other mappings
    }

