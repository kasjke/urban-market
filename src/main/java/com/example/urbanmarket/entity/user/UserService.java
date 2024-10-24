package com.example.urbanmarket.entity.user;

import com.example.urbanmarket.dto.request.UserRequestDto;
import com.example.urbanmarket.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto request);

    UserResponseDto getUserById(String id);

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(String id, UserRequestDto request);

    void deleteUser(String id);
}
