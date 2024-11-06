package com.example.urbanmarket.dto.response;



import java.time.Instant;
public record ReviewResponseDto(
        String id,
        String productId,
        String userId,
        String content,
        double rating,
        Instant createdAt
){}