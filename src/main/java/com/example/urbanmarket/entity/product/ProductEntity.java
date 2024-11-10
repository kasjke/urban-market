package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.entity.product.sections.Category;
import com.example.urbanmarket.entity.product.sections.SubCategory;
import com.example.urbanmarket.entity.user.review.ReviewEntity;
import com.example.urbanmarket.enums.ProductSize;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    private String id;

    @Size(max = 50)
    @NotNull
    private String name;

    @Size(min = 10, max = 200)
    @NotNull
    private String description;

    @NotNull
    private SubCategory subCategory;

    @NotNull
    private Category category;

    @NotNull
    private int currentPrice;

    @NotNull
    private int oldPrice;

    @NotNull
    private int amount;

    private List<String> images;

    private int purchaseCount;

    private String shopId;
 
    @CreatedDate
    private Instant createdAt;

    private List<ProductSize> product_sizes;

    @DBRef
    @JsonManagedReference
    private List<ReviewEntity> reviews = new ArrayList<>();

    private double averageRating;

    private Map<String, Integer> ratingDistribution = new HashMap<>();

    public void updateRating() {
        if (reviews == null || reviews.isEmpty()) {
            this.averageRating = 0.0;
            this.ratingDistribution.clear();
            return;
        }

        double avg = reviews.stream()
                .mapToDouble(ReviewEntity::getRating)
                .average()
                .orElse(0.0);

        this.averageRating = Math.round(avg * 10.0) / 10.0;

        this.ratingDistribution = reviews.stream()
                .collect(Collectors.groupingBy(
                        review -> String.valueOf(Math.round(review.getRating() * 10.0) / 10.0).replace(".", "_"),
                        Collectors.reducing(0, e -> 1, Integer::sum)
                ));
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = Objects.requireNonNullElseGet(reviews, ArrayList::new);
        updateRating();
    }
    public ProductEntity(String name, String description, SubCategory subCategory, int currentPrice, int amount, List<String> images, String shopId, List<ReviewEntity> reviews) {
        this.name = name;
        this.description = description;
        this.subCategory = subCategory;
        this.category = subCategory.getCategory();
        this.currentPrice = currentPrice;
        this.oldPrice = this.currentPrice;
        this.amount = amount;
        this.images = images;
        this.shopId = shopId;
        this.createdAt = Instant.now();
        this.reviews = reviews;
    }
}
