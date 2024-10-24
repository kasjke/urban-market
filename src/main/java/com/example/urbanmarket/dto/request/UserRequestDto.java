package com.example.urbanmarket.dto.request;

public record UserRequestDto(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String password
) {}