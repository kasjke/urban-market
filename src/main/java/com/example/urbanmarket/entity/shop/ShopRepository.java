package com.example.urbanmarket.entity.shop;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShopRepository extends MongoRepository<ShopEntity, String> {
    Optional<ShopEntity> findByName(String name);
}
