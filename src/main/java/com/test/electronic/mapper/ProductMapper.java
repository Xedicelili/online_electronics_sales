
package com.test.electronic.mapper;

import com.test.electronic.model.dto.request.ProductRequest;
import com.test.electronic.model.dto.response.OrderResponse;
import com.test.electronic.model.dto.response.ProductResponse;
import com.test.electronic.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    OrderResponse.ProductResponse toProductResponse(Product product);
    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductRequest request);
    void updateEntity(ProductRequest request, @MappingTarget Product product);




//    @Mappings({
//            @Mapping(target = "name", source = "name")
//    })
//    Product toProduct(ComparisonRequest.ProductDetail productDetail);

    ProductResponse toResponse(Product product);


}





