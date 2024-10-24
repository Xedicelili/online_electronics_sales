package com.test.electronic.service.impl;

import com.test.electronic.exception.UserAlreadyExistsException;
import com.test.electronic.mapper.UserMapper;
import com.test.electronic.model.dto.ExceptionDTO;
import com.test.electronic.model.dto.request.LoginReq;
import com.test.electronic.model.dto.request.UserRequest;
import com.test.electronic.model.dto.response.LoginRes;
import com.test.electronic.model.dto.response.UserResponse;
import com.test.electronic.model.entity.Authority;
import com.test.electronic.model.entity.User;
import com.test.electronic.repository.UserRepository;
import com.test.electronic.service.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;

    private static final String EMAIL_VERIFICATION_SUBJECT = "Email Verification";
    private static final String EMAIL_VERIFICATION_TEXT = "Please verify your email using this code: ";


    public ResponseEntity<?> authenticate(LoginReq loginReq) {
        log.info("Authenticate method started by: {}", loginReq.getUsername());

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()));

            log.info("Password is correct for user: {}", loginReq.getUsername());
            String username = authentication.getName();
            User user = new User(username, "");
            String token = jwtUtil.createToken(user);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            LoginRes loginRes = new LoginRes(username, token);
            log.info("User: {} successfully logged in", user.getUsername());
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(loginRes);

        } catch (BadCredentialsException e) {

            log.info("Password is incorrect for user: {}", loginReq.getUsername());

            Optional<User> userOptional = userRepository.findByEmail(loginReq.getUsername());

            if (userOptional.isPresent()) {
                log.info("User found with email: {}", loginReq.getUsername());
                User user = userOptional.get();


                String verificationCode = UUID.randomUUID().toString();
                user.setVerificationCode(verificationCode);
                userRepository.save(user);

                String subject = "Verification Code";
                String text = "Your verification code is: " + verificationCode;

                try {
                    emailService.sendEmail(user.getEmail(), subject, text);
                    log.info("Verification code sent to: {}", user.getEmail());
                } catch (Exception emailException) {
                    log.error("Failed to send verification email to: {}", user.getEmail(), emailException.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failed to send verification code due to server error.");
                }

                ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.BAD_REQUEST.value(),
                        "Password incorrect. Verification code sent to your email.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
            }

            ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), "Password is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
        } catch (Exception e) {
            ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            log.error("Error due to {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
        }
    }

    public UserResponse registerUser(UserRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            log.info("User with email: {} already exists. No verification code sent.", request.getEmail());
            throw new UserAlreadyExistsException("User with this email already exists.");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setVerificationCode(UUID.randomUUID().toString());
        user.setIsVerified(false);

        Authority authority = new Authority("ADMIN");
        Set<Authority> authoritySet = Set.of(authority);
        user.setAuthorities(authoritySet);
        userRepository.save(user);

        emailService.sendEmail(user.getEmail(), EMAIL_VERIFICATION_SUBJECT, EMAIL_VERIFICATION_TEXT + user.getVerificationCode());
        log.info("User registered successfully with email: " + user.getEmail());
        return userMapper.toResponse(user);
    }


}


