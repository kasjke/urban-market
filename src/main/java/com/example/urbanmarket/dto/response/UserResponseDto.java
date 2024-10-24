package com.example.urbanmarket.dto.response;




public record UserResponseDto(
        String id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
}
