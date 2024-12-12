package com.example.urbanmarket.entity.shop;

import com.example.urbanmarket.entity.product.ProductEntity;
import com.example.urbanmarket.entity.shop.contacts.ContactInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Document(collection = "shops")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopEntity {
    @Id
    private String id;

    @NotNull
    private String name;

    private String description;

    private String logo;

    private ContactInfo contacts;

    private int sold;

    private int positiveReviews;

    @DBRef
    private List<ProductEntity> products;

    @CreatedDate
    private Date createdAt;



    public ShopEntity(String name, String description, String logo, ContactInfo contacts, List<ProductEntity> products) {
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.contacts = contacts;
        this.products = Objects.requireNonNullElseGet(products, ArrayList::new);
        updateStatistics();
    }

    public void addProduct(ProductEntity product) {
        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("Product or Product ID cannot be null.");
        }
        if (products == null) {
            products = new ArrayList<>();
        }
        if (!products.contains(product)) {
            products.add(product);
            sold += product.getPurchaseCount();
            if (product.getAverageRating() >= 4.0) {
                positiveReviews++;
            }
        }
    }

    public void removeProduct(String productId) {
        if (products != null && !products.isEmpty()) {
            products.removeIf(product -> {
                boolean match = product.getId().equals(productId);
                if (match) {
                    sold -= product.getPurchaseCount();
                    if (product.getAverageRating() >= 4.0) {
                        positiveReviews--;
                    }
                }
                return match;
            });
        }
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = Objects.requireNonNullElseGet(products, ArrayList::new);
        updateStatistics();
    }

    private void updateStatistics() {
        updateSoldCount();
        updatePositiveReviews();
    }

    private void updateSoldCount() {
        this.sold = CollectionUtils.isEmpty(products)
                ? 0
                : products.stream().mapToInt(ProductEntity::getPurchaseCount).sum();
    }

    public void updatePositiveReviews() {
        this.positiveReviews = CollectionUtils.isEmpty(products)
                ? 0
                : (int) products.stream()
                .flatMap(product -> product.getReviews().stream())
                .filter(review -> review.getRating() >= 4)
                .count();
    }

    public int calculateSold() {
        return CollectionUtils.isEmpty(products)
                ? 0
                : products.stream().mapToInt(ProductEntity::getPurchaseCount).sum();
    }


    public int calculatePositiveReviews() {
        return CollectionUtils.isEmpty(products)
                ? 0
                : (int) products.stream()
                .flatMap(product -> product.getReviews().stream())
                .filter(review -> review.getRating() >= 4)
                .count();
    }
}
