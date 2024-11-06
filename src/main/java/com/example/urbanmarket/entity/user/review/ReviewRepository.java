package com.example.urbanmarket.entity.user.review;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<ReviewEntity, String> {
    List<ReviewEntity> findByProductId(String productId);
}