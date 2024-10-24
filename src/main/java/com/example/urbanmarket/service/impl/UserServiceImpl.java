package com.example.urbanmarket.service.impl;

import com.example.urbanmarket.dto.request.UserRequest;
import com.example.urbanmarket.dto.response.UserResponse;
import com.example.urbanmarket.entity.User;
import com.example.urbanmarket.exception.userExceptions.UserAlreadyExists;
import com.example.urbanmarket.exception.userExceptions.UserNotFoundException;
import com.example.urbanmarket.mapper.UserMapper;
import com.example.urbanmarket.repository.UserRepository;
import com.example.urbanmarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExists(request.email());
        }

        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return userMapper.toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse updateUser(String id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        userMapper.updateUserFromDto(request, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }
}