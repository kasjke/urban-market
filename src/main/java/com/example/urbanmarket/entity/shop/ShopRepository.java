package com.example.urbanmarket.entity.shop;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShopRepository extends MongoRepository<ShopEntity, String> {
}
