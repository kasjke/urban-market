package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.dto.request.product.ProductInCartRequestDto;
import com.example.urbanmarket.dto.request.product.ProductRequestDto;
import com.example.urbanmarket.dto.response.product.ProductInCartOrderResponseDto;
import com.example.urbanmarket.dto.response.product.ProductResponseDto;
import com.example.urbanmarket.entity.shop.ShopServiceImpl;
import com.example.urbanmarket.entity.user.review.ReviewEntity;
import com.example.urbanmarket.exception.exceptions.CustomNotFoundException;
import com.example.urbanmarket.exception.LogEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final String OBJECT_NAME = "Product";
    private final ShopServiceImpl shopService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto create(ProductRequestDto productDto) {
        ProductEntity entity = productMapper.toEntity(productDto);
        entity = productRepository.save(entity);
        shopService.addProductToShop(entity);

        log.info("{}: " + OBJECT_NAME + " (Id: {}) was created", LogEnum.SERVICE, entity.getId());
        return productMapper.toResponseDto(entity);
    }

    @Override
    public ProductResponseDto getById(String id) {
        ProductEntity entity = findById(id);

        log.info("{}: " + OBJECT_NAME + " (Id: {}) was found", LogEnum.SERVICE, id);
        return productMapper.toResponseDto(entity);
    }

    @Override
    public List<ProductResponseDto> getAll() {
        List<ProductEntity> entities = productRepository.findAll();

        log.info("{}: all " + OBJECT_NAME + "were obtained", LogEnum.SERVICE);
        return productMapper.toResponseDtoList(entities);
    }

    @Override
    public Page<ProductResponseDto> findByOldPriceGreaterThanCurrentPrice(Pageable pageable) {
        Page<ProductEntity> allProducts = productRepository.findAll(pageable);

        List<ProductEntity> filteredProducts = allProducts.stream()
                .filter(product -> product.getOldPrice() > product.getCurrentPrice())
                .toList();

        List<ProductResponseDto> productResponseDtos = productMapper.toResponseDtoList(filteredProducts);

        log.info("{}: Found {} products with old price greater than current price", LogEnum.SERVICE, filteredProducts.size());
        return new PageImpl<>(productResponseDtos, pageable, allProducts.getTotalElements());
    }

    @Override
    public Page<ProductResponseDto> getNewArrivals(Pageable pageable) {
        Page<ProductResponseDto> newArrivals = productRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(productMapper::toResponseDto);
        log.info("{}: Retrieved {} new arrival products", LogEnum.SERVICE, newArrivals.getTotalElements());
        return newArrivals;
    }

    @Override
    public Page<ProductResponseDto> getBestSellers(Pageable pageable) {
        Page<ProductResponseDto> bestSellers = productRepository.findAllByOrderByPurchaseCountDesc(pageable)
                .map(productMapper::toResponseDto);
        log.info("{}: Retrieved {} best-selling products", LogEnum.SERVICE, bestSellers.getTotalElements());
        return bestSellers;
    }

    @Override
    public ProductResponseDto update(String id, ProductRequestDto productDto) {
        ProductEntity fromDb = findById(id);
        ProductEntity entity = productMapper.toEntity(productDto);
        entity.setId(fromDb.getId());

        productRepository.save(entity);
        shopService.addProductToShop(entity);
        log.info("{}: " + OBJECT_NAME + " (id: {}) was updated", LogEnum.SERVICE, id);
        return productMapper.toResponseDto(entity);
    }

    @Override
    public void delete(String id) {
        shopService.removeProductFromShop(findById(id));
        productRepository.deleteById(id);

        log.info("{}: " + OBJECT_NAME + " (id: {}) was deleted", LogEnum.SERVICE, id);
    }

    public boolean existById(String id) {
        boolean exists = productRepository.existsById(id);
        log.info("{}: {} (Id: {}) existence check result: {}", LogEnum.SERVICE, OBJECT_NAME, id, exists);
        return exists;
    }

    public ProductEntity findById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("{}: {} (Id: {}) not found", LogEnum.SERVICE, OBJECT_NAME, productId);
                    return new CustomNotFoundException(OBJECT_NAME, productId);
                });
    }

    public List<ProductEntity> findByIds(List<String> ids) {
        List<ProductEntity> products = ids.stream()
                .map(this::findById)
                .collect(Collectors.toList());
        log.info("{}: Found {} products by Ids: {}", LogEnum.SERVICE, products.size(), ids);
        return products;
    }

    public List<ProductInCartOrderResponseDto> getCartOrderResponseProducts(List<ProductInCartRequestDto> products) {
        List<ProductInCartOrderResponseDto> cartOrderResponse = products
                .stream()
                .map(productDto -> {
                    ProductEntity product = findById(productDto.id());
                    return new ProductInCartOrderResponseDto(
                            product.getId(),
                            product.getName(),
                            product.getImages().get(0),
                            productDto.amount(),
                            product.getCurrentPrice()
                    );
                })
                .toList();
        log.info("{}: Cart order response products built for {} products", LogEnum.SERVICE, cartOrderResponse.size());
        return cartOrderResponse;
    }


    //REVIEW IN PRODUCT
    public void addReviewToProduct(ReviewEntity review) {
        ProductEntity product = findById(review.getProductId());
        List<ReviewEntity> reviewList = product.getReviews();
        if (reviewList == null) {
            reviewList = new ArrayList<>();
        }
        reviewList.add(review);
        product.setReviews(reviewList);
        productRepository.save(product);
    }

    public void removeReviewFromProduct(ReviewEntity review) {
        ProductEntity product = findById(review.getProductId());
        List<ReviewEntity> reviewList = product.getReviews();

        assert reviewList != null;
        if (reviewList.contains(review)) {
            reviewList.remove(review);
            product.setReviews(reviewList);
            productRepository.save(product);
        }else {
            throw new CustomNotFoundException("Review in the product's review list", review.getId());
        }
    }
}