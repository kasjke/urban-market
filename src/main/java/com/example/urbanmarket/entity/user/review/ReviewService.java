package com.example.urbanmarket.entity.user.review;

import com.example.urbanmarket.dto.request.ReviewRequestDto;
import com.example.urbanmarket.dto.response.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto create(ReviewRequestDto reviewDto);

    ReviewResponseDto getById(String reviewId);

    List<ReviewResponseDto> getAll();
    List<ReviewResponseDto> getByProductId(String productId);

    void delete(String reviewId);


}
