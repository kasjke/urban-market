package com.example.urbanmarket.entity.user.review;

import com.example.urbanmarket.dto.request.ReviewRequestDto;
import com.example.urbanmarket.dto.response.ReviewResponseDto;
import com.example.urbanmarket.entity.product.ProductEntity;
import com.example.urbanmarket.entity.product.ProductServiceImpl;
import com.example.urbanmarket.exception.exceptions.general.CustomNotFoundException;
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

    private final ProductServiceImpl productService;

    @Override
    @Transactional
    public ReviewResponseDto create(ReviewRequestDto reviewDto) {
        ReviewEntity review = reviewMapper.toEntity(reviewDto);

        review.setCreatedAt(Instant.now());
        review.setProductId(reviewDto.productId());
        review = reviewRepository.save(review);

        productService.addReviewToProduct(review);

        log.info("{}: {} (Id: {}) was created for Product (Id: {})", LogEnum.SERVICE, OBJECT_NAME, review.getId(), review.getProductId());
        return reviewMapper.toResponseDto(review);
    }

    @Transactional(readOnly = true)
    @Override
    public ReviewResponseDto getById(String reviewId) {
        if (reviewId == null || reviewId.trim().isEmpty()) {
            log.error("{}: Invalid {} Id: '{}'", LogEnum.SERVICE, OBJECT_NAME, reviewId);
            throw new IllegalArgumentException(OBJECT_NAME + reviewId);
        }

        ReviewResponseDto reviewResponseDto = reviewMapper.toResponseDto(findById(reviewId));

        log.info("{}: {} (Id: {}) was found", LogEnum.SERVICE, OBJECT_NAME, reviewId);
        return reviewResponseDto;
    }

    @Override
    public List<ReviewResponseDto> getAll() {
        reviewRepository.findAll();
        return List.of();
    }

    @Override
    public List<ReviewResponseDto> getByProductId(String productId) {
        ProductEntity product = productService.findById(productId);

        if (product.getReviews() == null || product.getReviews().isEmpty()) {
            throw new CustomNotFoundException(OBJECT_NAME, "Product", productId);
        }

        List<ReviewResponseDto> reviewDtoList = reviewMapper.toResponseDtoList(reviewRepository.findAll());
        log.info("{}: Retrieved all {} for Product (Id: {})", LogEnum.SERVICE, OBJECT_NAME, productId);
        return reviewDtoList;
    }

    @Override
    @Transactional
    public void delete(String reviewId) {
        ReviewEntity review = findById(reviewId);

        productService.removeReviewFromProduct(review);
        reviewRepository.delete(review);

        log.info("{}: {} (Id: {}) was deleted from Product (Id: {})", LogEnum.SERVICE, OBJECT_NAME, reviewId, review.getProductId());
    }

    public ReviewEntity findById(String id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("{}: {} (Id: {}) not found", LogEnum.SERVICE, OBJECT_NAME, id);
                    return new CustomNotFoundException(OBJECT_NAME, id);
                });
    }
}
