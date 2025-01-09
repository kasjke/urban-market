package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.entity.product.sections.SubCategory;
import com.example.urbanmarket.enums.Color;
import com.example.urbanmarket.enums.ProductSize;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<ProductEntity, String> {


    List<ProductEntity> findAllByOrderByPurchaseCountDesc();

    List<ProductEntity> findBySubCategoryAndIdNot(SubCategory subCategory, String id);

    @Query("{ 'subCategory.id': ?0 }")
    List<ProductEntity> findByCategoryId(String categoryId);

    @Query("{ 'currentPrice': { $gte: ?0, $lte: ?1 } }")
    List<ProductEntity> findByPriceRange(Integer priceMin, Integer priceMax);

    @Query("{ 'color': ?0 }")
    List<ProductEntity> findByColor(Color color);

    @Query("{ 'product_sizes': ?0 }")
    List<ProductEntity> findBySize(ProductSize size);

    List<ProductEntity> findAllByOrderByCreatedAtDesc();
}
