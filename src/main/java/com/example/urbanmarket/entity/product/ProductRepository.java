package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.entity.product.sections.Category;
import com.example.urbanmarket.entity.product.sections.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<ProductEntity, String> {
    List<ProductEntity> findByCategoryAndIdNot(Category category, String productId);
    Page<ProductEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ProductEntity> findAllByOrderByPurchaseCountDesc(Pageable pageable);

    List<ProductEntity> findBySubCategoryAndIdNot(SubCategory subCategory, String id);
}
