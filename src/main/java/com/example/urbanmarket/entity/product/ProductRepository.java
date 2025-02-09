package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.entity.product.sections.SubCategory;
import com.example.urbanmarket.enums.Color;
import com.example.urbanmarket.enums.ProductSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<ProductEntity, String> {


    List<ProductEntity> findAllByOrderByPurchaseCountDesc();

    List<ProductEntity> findBySubCategoryAndIdNot(SubCategory subCategory, String id);

    Page<ProductEntity> findAllByOrderByCurrentPriceAsc(Pageable pageable);

    Page<ProductEntity> findAllByOrderByCurrentPriceDesc(Pageable pageable);

    @Query("{ 'subCategory': ?0 }")
    Page<ProductEntity> findBySubCategoryName(String categoryName, Pageable pageable);

    @Query("{ 'currentPrice': { $gte: ?0, $lte: ?1 } }")
    Page<ProductEntity> findByPriceRange(Integer priceMin, Integer priceMax, Pageable pageable);

    @Query("{ 'color': ?0 }")
    Page<ProductEntity> findByColor(Color color, Pageable pageable);

    @Query("{ 'product_sizes': ?0 }")
    Page<ProductEntity> findBySize(ProductSize size, Pageable pageable);

    Page<ProductEntity> findAll(Pageable pageable);

    List<ProductEntity> findAllByOrderByCreatedAtDesc();
    List<ProductEntity> findBySubCategoryIn(List<SubCategory> subCategories);
}
