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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    private ContactInfo contacts;

    private int sold;

    private int positiveReviews;

    @DBRef
    private List<ProductEntity> products;

    @CreatedDate
    private Date createdAt;

    private void updateStatistics(){
        int positiveRev = 0;
        int negativeRev = 0;

        for (ProductEntity elem:products){
            sold+=elem.getPurchaseCount();
            double rating = elem.getAverageRating();

            if (rating>=4.0){
                positiveRev++;
            } else {
                negativeRev++;
            }
        }

        positiveReviews = 100/(positiveRev+negativeRev)*positiveReviews;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = Objects.requireNonNullElseGet(products, ArrayList::new);
        updateStatistics();
    }

    public ShopEntity(String name, String description, String logo, ContactInfo contacts, List<ProductEntity> products) {
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.contacts = contacts;
        setProducts(products);
    }

    public void addProduct(ProductEntity product){
        if (isNotEmptyProductList()){
            if (!products.contains(product)){
                this.products.add(product);
                updateStatistics();
            }
        }
    }

    public void removeProduct(String productId) {
        this.products.removeIf(product -> product.getId().equals(productId));
        updateStatistics();
    }

    private boolean isNotEmptyProductList(){
        return this.products!=null&&!this.products.isEmpty();
    }
}
