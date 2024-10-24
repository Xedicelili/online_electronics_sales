package com.test.electronic.mapper;

import com.test.electronic.model.dto.request.PromotionRequest;
import com.test.electronic.model.dto.response.PromotionResponse;
import com.test.electronic.model.entity.Promotion;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    @Mapping(source = "category.name", target = "categoryName")
    PromotionResponse toResponse(Promotion promotion);

    @Mapping(target = "category", ignore = true)
    Promotion toEntity(PromotionRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    void updateEntity(PromotionRequest request, @MappingTarget Promotion promotion);
}



