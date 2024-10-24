package com.example.urbanmarket.dto.request;

public record UserRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String password
) {}