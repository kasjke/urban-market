package com.example.urbanmarket.entity.cart;

import com.example.urbanmarket.entity.user.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<CartEntity, String> {
    boolean existsByUser(UserEntity user);

    CartEntity findByUser(UserEntity user);
}
