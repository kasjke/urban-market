package com.example.urbanmarket.entity.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class ProductEntity {

    @Id
    private String id;

    @Size(max = 50)
    @NotNull
    private String name;

    @Size(min = 10, max = 200)
    @NotNull
    private String description;

//    @DBRef
//    private CategoryEntity category;
//
//    @DBRef
//    private SubCategoryEntity subCategory;

    //    @Column
//    private Labels label;
//
//    @Column
//    private Types type;


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
}
