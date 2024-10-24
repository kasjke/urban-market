package com.example.urbanmarket.entity.order;

import com.example.urbanmarket.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Data
@NoArgsConstructor
public class OrderEntity {

    @Id
    private String id;

    private String userId;

    private List<String> productIds;

    private int totalPrice;

    private OrderStatus status;

    private String shippingAddress;


    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime  updatedAt;
}
