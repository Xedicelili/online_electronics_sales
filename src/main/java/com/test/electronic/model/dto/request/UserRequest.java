package com.test.electronic.model.dto.request;

import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;

}
