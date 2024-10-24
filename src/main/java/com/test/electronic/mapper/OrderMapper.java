
package com.test.electronic.mapper;

import com.test.electronic.model.dto.request.OrderRequest;
import com.test.electronic.model.dto.response.OrderResponse;
import com.test.electronic.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
 public interface  OrderMapper {

   OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

   Order toOrder(OrderRequest request);


   OrderResponse toOrderResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    void updateOrderFromRequest(OrderRequest request, @MappingTarget Order order);


    }




