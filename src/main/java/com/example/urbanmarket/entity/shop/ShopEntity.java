package com.example.urbanmarket.entity.shop;

import com.example.urbanmarket.entity.product.ProductEntity;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "carts")
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

    @DBRef
    private List<ProductEntity> products;

    @CreatedDate
    private Date createdAt;

    public ShopEntity(String name, String description, String logo, List<ProductEntity> products) {
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.products = products;
    }

    public void addProduct(ProductEntity product){
        if (isNotEmptyProductList()){
            if (!products.contains(product)){
                this.products.add(product);
            }
        }
    }

    public void removeProduct(String productId) {
        this.products.removeIf(product -> product.getId().equals(productId));
    }

    private boolean isNotEmptyProductList(){
        return this.products!=null&&!this.products.isEmpty();
    }
}
