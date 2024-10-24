package com.example.urbanmarket.service;

import com.example.urbanmarket.dto.request.UserRequest;
import com.example.urbanmarket.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);

    UserResponse getUserById(String id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(String id, UserRequest request);

    void deleteUser(String id);
}
