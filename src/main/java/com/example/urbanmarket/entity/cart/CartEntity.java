package com.example.urbanmarket.entity.cart;

import com.example.urbanmarket.dto.response.product.ProductInCartOrderResponseDto;
import com.example.urbanmarket.entity.user.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity {
    @Id
    private String id;

    private List<ProductInCartOrderResponseDto> products;

    @DBRef
    private UserEntity user;

    @NotNull
    private int totalPrice;

    public void setTotalPrice(int discount) {
        int totalPrice = -discount;
        for (ProductInCartOrderResponseDto product : products) {
            totalPrice += product.price()*product.amount();

            if (totalPrice<0){
                totalPrice = 0;
                break;
            }
        }
        this.totalPrice = totalPrice;
    }

    public CartEntity(UserEntity user, List<ProductInCartOrderResponseDto> products, int discount) {
        this.user = user;
        this.products = products;

        setTotalPrice(discount);
    }
}
