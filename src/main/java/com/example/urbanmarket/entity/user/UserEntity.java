package com.example.urbanmarket.entity.user;

import com.example.urbanmarket.entity.order.OrderEntity;
import com.example.urbanmarket.entity.user.review.ReviewEntity;
import com.example.urbanmarket.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntity {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String phoneNumber;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @DBRef
    @JsonManagedReference
    private List<ReviewEntity> reviews;
    @DBRef
    private List<OrderEntity> orderHistory;

    public UserEntity(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}