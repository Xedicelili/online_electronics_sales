package com.test.electronic.mapper;


import com.test.electronic.model.dto.request.UserRequest;
import com.test.electronic.model.dto.response.UserResponse;
import com.test.electronic.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface  UserMapper {

    UserResponse toResponse(User user);
    User toEntity(UserRequest request);

   // UserResponse toResponse(String email);
}
