package com.example.urbanmarket.entity.user;

import com.example.urbanmarket.dto.request.UserRequestDto;
import com.example.urbanmarket.dto.request.auth.SignupRequestDto;
import com.example.urbanmarket.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto create(SignupRequestDto request);

    UserResponseDto getById(String id);

    List<UserResponseDto> getAll();

    UserResponseDto update(String id, UserRequestDto request);

    void delete(String id);
}
