package com.example.urbanmarket.dto.response;


public record UserResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
}
