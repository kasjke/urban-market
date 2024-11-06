package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.dto.request.product.ProductInCartRequestDto;
import com.example.urbanmarket.dto.request.product.ProductRequestDto;
import com.example.urbanmarket.dto.response.ReviewResponseDto;
import com.example.urbanmarket.dto.response.product.ProductInCartOrderResponseDto;
import com.example.urbanmarket.dto.response.product.ProductResponseDto;
import com.example.urbanmarket.dto.response.product.ProductResponseYouMayAlsoDto;
import com.example.urbanmarket.entity.shop.ShopServiceImpl;
import com.example.urbanmarket.entity.user.review.ReviewMapper;
import com.example.urbanmarket.exception.CustomNotFoundException;
import com.example.urbanmarket.exception.LogEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    private final ReviewMapper reviewMapper;

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

    public ProductResponseDto findProductWithReviewsById(String productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, productId));

        List<ReviewResponseDto> reviewDtos = productEntity.getReviews().stream()
                .map(reviewMapper::toResponseDto)
                .collect(Collectors.toList());

        List<ProductEntity> similarProducts = productRepository.findByCategoryAndIdNot(productEntity.getCategory(), productId);
        List<ProductResponseYouMayAlsoDto> similarProductDtos = similarProducts.stream()
                .map(productMapper::toYouMayAlsoDto)
                .collect(Collectors.toList());

        log.info("{}: {} with Id: {} was found along with reviews and similar products", LogEnum.SERVICE, OBJECT_NAME, productId);
        return productMapper.toResponseDto(productEntity, reviewDtos, similarProductDtos);
    }


    public List<ProductEntity> findByIds(List<String> ids) {
        List<ProductEntity> products = ids.stream()
                .map(this::findById)
                .collect(Collectors.toList());
        log.info("{}: Found {} products by Ids: {}", LogEnum.SERVICE, products.size(), ids);
        return products;
    }

    public List<ProductInCartOrderResponseDto> getCartOrderResponseFigures(List<ProductInCartRequestDto> products) {
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
        log.info("{}: Cart order response figures built for {} products", LogEnum.SERVICE, cartOrderResponse.size());
        return cartOrderResponse;
    }

    @Override
    public Page<ProductResponseDto> findByOldPriceGreaterThanCurrentPrice(Pageable pageable) {
        Page<ProductEntity> allProducts = productRepository.findAll(pageable);

        List<ProductEntity> filteredProducts = allProducts.stream()
                .filter(product -> product.getOldPrice() > product.getCurrentPrice())
                .toList();

        List<ProductResponseDto> productResponseDtos = filteredProducts.stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());

        log.info("{}: Found {} products with old price greater than current price", LogEnum.SERVICE, filteredProducts.size());
        return new PageImpl<>(productResponseDtos, pageable, allProducts.getTotalElements());
    }

    @Override
    public Page<ProductResponseDto> getNewArrivals(Pageable pageable) {
        log.info("{}: Retrieving new arrivals for {}", LogEnum.SERVICE, OBJECT_NAME);
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

}