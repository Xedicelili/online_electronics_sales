package com.test.electronic.mapper;

import com.test.electronic.model.dto.request.CardDetailsRequest;
import com.test.electronic.model.entity.CardDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardDetailsMapper {

    @Mapping(target = "userId", source = "userId")
    CardDetails toEntity(CardDetailsRequest cardDetailsRequest, Long userId);

    CardDetailsRequest toDto(CardDetails cardDetails);

    CardDetails toEntity(CardDetailsRequest cardDetailsRequest);

}




