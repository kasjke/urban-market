package com.example.urbanmarket.entity.user.auth;

import com.example.urbanmarket.dto.request.auth.LoginRequestDto;
import com.example.urbanmarket.dto.request.auth.SignupRequestDto;
import com.example.urbanmarket.dto.response.UserResponseDto;

public interface AuthService {

    UserResponseDto signUp(SignupRequestDto signupRequestDto);

    String login(LoginRequestDto loginRequestDto) throws Exception;
    /*
    UserResponseDto emailVerification(String emailVerificationCode);

    UserResponseDto passwordVerification(String passwordVerificationCode);

    UserResponseDto sentEmailVerifMes(String email);

    UserResponseDto sentPasswordVerifMes(String email);

    UserResponseDto resetPassword(LoginRequestDto loginRequestDto);

     */
}
