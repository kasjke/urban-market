package com.example.urbanmarket.entity.user.review;

import com.example.urbanmarket.dto.request.ReviewRequestDto;
import com.example.urbanmarket.dto.response.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto createReview(ReviewRequestDto reviewDto);

    List<ReviewResponseDto> getReviewsByProductId(String productId);

    void deleteReview(String productId, String reviewId);

    ReviewResponseDto getReviewById(String reviewId);
}
