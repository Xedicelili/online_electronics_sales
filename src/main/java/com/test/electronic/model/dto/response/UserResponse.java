package com.test.electronic.model.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserResponse {
    //private Long userId;
    private String name;
    private String surname;
    private String email;
    private String username;



}
