package com.example.urbanmarket.controller;

import com.example.urbanmarket.dto.request.ReviewRequestDto;
import com.example.urbanmarket.dto.response.ReviewResponseDto;
import com.example.urbanmarket.entity.user.review.ReviewService;
import com.example.urbanmarket.exception.LogEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private static final String OBJECT_NAME = "Review";
    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Create a new review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RuntimeException.class)))
    })
    public ResponseEntity<ReviewResponseDto> create(
            @Valid @RequestBody ReviewRequestDto reviewDto) {
        ReviewResponseDto createdReview = reviewService.createReview(reviewDto);
        log.info("{}: {} for product (id: {}) has been created", LogEnum.CONTROLLER, OBJECT_NAME, reviewDto.productId());
        return ResponseEntity.ok(createdReview);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Retrieve reviews for a product by product ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of reviews retrieved",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReviewResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RuntimeException.class)))
    })
    public ResponseEntity<List<ReviewResponseDto>> getByProductId(@PathVariable String productId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByProductId(productId);
        log.info("{}: {}s for product (id: {}) have been retrieved", LogEnum.CONTROLLER, OBJECT_NAME, productId);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Delete a review by review ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RuntimeException.class)))
    })
    public ResponseEntity<Void> delete(
            @RequestParam String productId,
            @PathVariable String reviewId) {
        reviewService.deleteReview(productId, reviewId);
        log.info("{}: {} (id: {}) for product (id: {}) has been deleted", LogEnum.CONTROLLER, OBJECT_NAME, reviewId, productId);
        return ResponseEntity.noContent().build();
    }
}
