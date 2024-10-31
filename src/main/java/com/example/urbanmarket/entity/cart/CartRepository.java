package com.example.urbanmarket.entity.cart;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<CartEntity, String> {
}
