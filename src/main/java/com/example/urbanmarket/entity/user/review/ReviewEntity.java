package com.example.urbanmarket.entity.user.review;

import com.example.urbanmarket.entity.product.ProductEntity;
import com.example.urbanmarket.entity.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {

    @Id
    private String id;
    private String productId;
    private String userId;
    private String content;
    private double rating;
    @CreatedDate
    private Instant createdAt;

    @JsonBackReference
    private ProductEntity product;

    @JsonBackReference
    private UserEntity user;
}
