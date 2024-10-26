package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.entity.product.sections.Category;
import com.example.urbanmarket.entity.product.sections.SubCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    private String id;

    @Size (max = 50)
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

    @NotNull
    private String shopId;

    @NotNull
    @CreatedDate
    private Date createdAt;

    public ProductEntity(String name, String description, SubCategory subCategory, int currentPrice, int amount, List<String> images, String shopId) {
        this.name = name;
        this.description = description;
        this.subCategory = subCategory;
        this.category = subCategory.getCategory();
        this.oldPrice = this.currentPrice;
        this.currentPrice = currentPrice;
        this.amount = amount;
        this.images = images;
        this.shopId = shopId;
    }
}
