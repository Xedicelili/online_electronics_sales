package com.test.electronic.service.impl;

import com.test.electronic.exception.NotFoundException;
import com.test.electronic.exception.UserNotFoundException;
import com.test.electronic.mapper.UserMapper;
import com.test.electronic.model.dto.request.UserRequest;
import com.test.electronic.model.dto.response.UserResponse;
import com.test.electronic.model.entity.User;
import com.test.electronic.repository.OrderRepository;
import com.test.electronic.repository.UserRepository;
import com.test.electronic.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Data
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OrderRepository orderRepository;
    private final UserMapper userMapper;


    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }



    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        user.setName(request.getName());
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

}
