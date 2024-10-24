package com.test.electronic.controller;

import com.test.electronic.model.dto.request.LoginReq;
import com.test.electronic.model.dto.request.UserRequest;
import com.test.electronic.model.dto.response.UserResponse;
import com.test.electronic.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
    @RequiredArgsConstructor
    @RequestMapping("/auth")
    public class AuthController {

        private final AuthService authService;

        @ResponseBody
        @PostMapping("/sign-in")
        public ResponseEntity<?> login(@RequestBody @Valid LoginReq loginReq)  {
            return authService.authenticate(loginReq);
        }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest request) {
        UserResponse response = authService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
