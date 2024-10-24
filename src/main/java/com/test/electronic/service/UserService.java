package com.test.electronic.service;


import com.test.electronic.model.dto.request.UserRequest;
import com.test.electronic.model.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse getById(Long id);

    UserResponse update(Long id, UserRequest request);

    void delete(Long id);

}
