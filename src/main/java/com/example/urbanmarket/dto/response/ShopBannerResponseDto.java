package com.example.urbanmarket.dto.response;


import java.util.Date;

public record ShopBannerResponseDto(
        String id,
        String name,
        String description,
        String logo,
        int sold,
        int positiveReviews,
        Date createdAt
) {}