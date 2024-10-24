package com.example.urbanmarket.entity.order;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<OrderEntity, String> {


    List<OrderEntity> findByUserId(String userId);
}
