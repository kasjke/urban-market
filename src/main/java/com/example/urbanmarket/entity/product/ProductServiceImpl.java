package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.dropbox.DropboxService;
import com.example.urbanmarket.dto.request.ProductAddDto;
import com.example.urbanmarket.dto.request.RequestUpdatePriceDto;
import com.example.urbanmarket.dto.request.product.ProductInCartRequestDto;
import com.example.urbanmarket.dto.request.product.ProductRequestDto;
import com.example.urbanmarket.dto.response.ResponseUpdatePriceDto;
import com.example.urbanmarket.dto.response.product.ProductInCartOrderResponseDto;
import com.example.urbanmarket.dto.response.product.ProductResponseDto;
import com.example.urbanmarket.dto.response.product.ProductResponseYouMayAlsoDto;
import com.example.urbanmarket.entity.product.sections.Category;
import com.example.urbanmarket.entity.product.sections.SubCategory;
import com.example.urbanmarket.entity.shop.ShopEntity;
import com.example.urbanmarket.entity.shop.ShopRepository;
import com.example.urbanmarket.entity.shop.ShopServiceImpl;
import com.example.urbanmarket.entity.user.review.ReviewEntity;
import com.example.urbanmarket.enums.Color;
import com.example.urbanmarket.enums.ProductSize;
import com.example.urbanmarket.exception.LogEnum;
import com.example.urbanmarket.exception.exceptions.general.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final String OBJECT_NAME = "Product";
    private final ShopServiceImpl shopService;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final ProductMapper productMapper;
    private final DropboxService dropboxService;

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

    public List<ProductResponseYouMayAlsoDto> findSimilarProducts(String productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, productId));

        List<ProductEntity> similarProducts = productRepository.findBySubCategoryAndIdNot(
                product.getSubCategory(), product.getId()
        );

        return productMapper.toResponseYouMayAlsoDtoList(similarProducts);
    }

    @Override
    public List<ProductResponseDto> findByOldPriceGreaterThanCurrentPrice() {
        List<ProductEntity> allProducts = productRepository
                .findAll()
                .stream()
                .toList();

        List<ProductEntity> filteredProducts = allProducts.stream()
                .filter(product -> product.getOldPrice() > product.getCurrentPrice())
                .toList();

        List<ProductResponseDto> productResponseDtos = productMapper.toResponseDtoList(filteredProducts);

        log.info("{}: Found {} products with old price greater than current price", LogEnum.SERVICE, filteredProducts.size());
        return productResponseDtos;
    }

    @Override
    public List<ProductResponseDto> getNewArrivals() {
        List<ProductResponseDto> newArrivals = productRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
        log.info("{}: Retrieved {} new arrival products", LogEnum.SERVICE, newArrivals);
        return newArrivals;
    }

    @Override
    public List<ProductResponseDto> getBestSellers() {
        List<ProductResponseDto> bestSellers = productRepository.findAllByOrderByPurchaseCountDesc()
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
        log.info("{}: Retrieved {} best-selling products", LogEnum.SERVICE, bestSellers);
        return bestSellers;
    }

    @Override
    public Page<ProductResponseDto> getFilteredProducts(
            String categoryName,
            String createdAt,
            String price,
            Integer priceMin,
            Integer priceMax,
            Color color,
            ProductSize size,
            Pageable pageable
    ) {
        Page<ProductEntity> filteredProducts;

        if (categoryName != null) {
            filteredProducts = productRepository.findBySubCategoryName(categoryName, pageable);
        } else if (priceMin != null && priceMax != null) {
            filteredProducts = productRepository.findByPriceRange(priceMin, priceMax, pageable);
        } else if (color != null) {
            filteredProducts = productRepository.findByColor(color, pageable);
        } else if (size != null) {
            filteredProducts = productRepository.findBySize(size, pageable);
        } else {
            filteredProducts = productRepository.findAll(pageable);
        }

        return filteredProducts.map(productMapper::toResponseDto);
    }
    @Override
    public Page<ProductResponseDto> getProductsSortedByPrice(String sortDirection, Pageable pageable) {
        Page<ProductEntity> products;

        if ("DESC".equalsIgnoreCase(sortDirection)) {
            products = productRepository.findAllByOrderByCurrentPriceDesc(pageable);
        } else {
            products = productRepository.findAllByOrderByCurrentPriceAsc(pageable);
        }

        return products.map(productMapper::toResponseDto);
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

    public List<ProductResponseDto> getProductsByCategories(String category) {
        try {
            Category categoryEnum = Category.valueOf(category.toUpperCase());
            List<SubCategory> subCategories = SubCategory.getSubcategoriesForMainCategory(categoryEnum);
            List<ProductEntity> products = productRepository.findBySubCategoryIn(subCategories);

            return products.stream()
                    .map(productMapper::toResponseDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category name: " + category);
        }
    }
    @Transactional
    public ResponseUpdatePriceDto updateProductPrice(String productId, RequestUpdatePriceDto requestUpdatePriceDto) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, productId));

        product.updatePrice(requestUpdatePriceDto.newPrice());
        productRepository.save(product);

        return new ResponseUpdatePriceDto(product.getOldPrice(), product.getCurrentPrice());
    }

    @Override
    public String addProduct(String productId, MultipartFile titleImageFile,
                             List<MultipartFile> additionalImageFiles) {
        if (titleImageFile == null || titleImageFile.isEmpty()) {
            throw new IllegalArgumentException("Title image file is null or empty");
        }

        ProductEntity product = findById(productId);

        String folderName = "/" + UUID.randomUUID();
        dropboxService.createFolder(folderName);

        String titleImageLink = dropboxService.uploadImage(
                folderName + "/title.png",
                titleImageFile
        );

        List<String> imageLinks = new ArrayList<>();
        imageLinks.add(titleImageLink);

        if (additionalImageFiles != null && !additionalImageFiles.isEmpty()) {
            AtomicInteger counter = new AtomicInteger(1);
            additionalImageFiles.forEach(imageFile -> {
                try {
                    String additionalImageLink = dropboxService.uploadImage(
                            folderName + "/" + counter.getAndIncrement() + ".png",
                            imageFile
                    );
                    imageLinks.add(additionalImageLink);
                } catch (Exception e) {
                    throw new RuntimeException("Error uploading additional image", e);
                }
            });
        }

        product.setImages(imageLinks);

        productRepository.save(product);
        shopService.addProductToShop(product);
        return titleImageLink;
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


    public void addReviewToProduct(ReviewEntity review) {
        ProductEntity product = findById(review.getProductId());
        List<ReviewEntity> reviewList = product.getReviews();
        if (reviewList == null) {
            reviewList = new ArrayList<>();
        }
        reviewList.add(review);
        product.setReviews(reviewList);
        productRepository.save(product);
        ShopEntity shop = shopRepository.findById(product.getShopId())
                .orElseThrow(() -> new CustomNotFoundException("Shop", product.getShopId()));
        shop.updatePositiveReviews();
        shopRepository.save(shop);
    }

    public void removeReviewFromProduct(ReviewEntity review) {
        ProductEntity product = findById(review.getProductId());
        List<ReviewEntity> reviewList = product.getReviews();

        if (reviewList == null) {
            throw new CustomNotFoundException("Review list is null for the product", product.getId());
        }
        if (reviewList.contains(review)) {
            reviewList.remove(review);
            product.setReviews(reviewList);
            productRepository.save(product);
        } else {
            throw new CustomNotFoundException("Review in the product's review list", review.getId());
        }
    }
}