package com.example.urbanmarket.entity.user.review;

import com.example.urbanmarket.dto.request.ReviewRequestDto;
import com.example.urbanmarket.dto.response.ReviewResponseDto;
import com.example.urbanmarket.entity.product.ProductEntity;
import com.example.urbanmarket.entity.product.ProductRepository;
import com.example.urbanmarket.exception.CustomNotFoundException;
import com.example.urbanmarket.exception.LogEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private static final String OBJECT_NAME = "Review";
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto reviewDto) {
        ProductEntity product = productRepository.findById(reviewDto.productId())
                .orElseThrow(() -> new CustomNotFoundException("Product", reviewDto.productId()));

        ReviewEntity review = reviewMapper.toEntity(reviewDto);
        review.setCreatedAt(Instant.now());
        review.setProductId(reviewDto.productId());
        ReviewEntity savedReview = reviewRepository.save(review);

        product.getReviews().add(savedReview);
        product.updateRating();
        productRepository.save(product);

        log.info("{}: {} (Id: {}) was created for Product (Id: {})", LogEnum.SERVICE, OBJECT_NAME, savedReview.getId(), product.getId());
        return reviewMapper.toResponseDto(savedReview);
    }

    @Override
    public List<ReviewResponseDto> getReviewsByProductId(String productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomNotFoundException("Product", productId));

        if (product.getReviews() == null || product.getReviews().isEmpty()) {
            throw new CustomNotFoundException(OBJECT_NAME, "Product", productId);
        }

        log.info("{}: Retrieved all {} for Product (Id: {})", LogEnum.SERVICE, OBJECT_NAME, productId);
        return product.getReviews().stream()
                .map(reviewMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteReview(String productId, String reviewId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomNotFoundException("Product", productId));

        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomNotFoundException("Review", reviewId));

        if (!review.getProductId().equals(productId)) {
            throw new IllegalArgumentException();
        }

        product.getReviews().removeIf(r -> r.getId().equals(reviewId));
        productRepository.save(product);
        reviewRepository.delete(review);

        log.info("{}: {} (Id: {}) was deleted from Product (Id: {})", LogEnum.SERVICE, OBJECT_NAME, reviewId, productId);
    }

    @Transactional(readOnly = true)
    @Override
    public ReviewResponseDto getReviewById(String reviewId) {
        if (reviewId == null || reviewId.trim().isEmpty()) {
            log.error("{}: Invalid {} Id: '{}'", LogEnum.SERVICE, OBJECT_NAME, reviewId);
            throw new IllegalArgumentException(OBJECT_NAME + reviewId);
        }

        ReviewResponseDto reviewResponseDto = reviewRepository.findById(reviewId)
                .map(reviewMapper::toResponseDto)
                .orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, reviewId));

        log.info("{}: {} (Id: {}) was found", LogEnum.SERVICE, OBJECT_NAME, reviewId);
        return reviewResponseDto;
    }
}
