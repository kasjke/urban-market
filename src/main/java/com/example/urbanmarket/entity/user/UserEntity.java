package com.example.urbanmarket.entity.user;

import com.example.urbanmarket.entity.order.OrderEntity;
import com.example.urbanmarket.entity.user.review.ReviewEntity;
import com.example.urbanmarket.enums.Role;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    @NotNull
    private boolean isEmailVerified;

    @NotNull
    private boolean isPasswordVerified;

    private String emailVerificationCode, passwordVerificationCode;


    public UserEntity(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        role = Role.USER;
        //recentlyViewed = new ArrayList<>();
        isEmailVerified = false;
        isPasswordVerified = false;
        emailVerificationCode = UUID.randomUUID().toString().substring(0, 6);;
        passwordVerificationCode = UUID.randomUUID().toString().substring(0, 6);;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password, role);
    }
}