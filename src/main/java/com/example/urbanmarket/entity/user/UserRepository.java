package com.example.urbanmarket.entity.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailVerificationCode(String emailVerificationCode);
    Optional<UserEntity> findByPasswordVerificationCode(String passwordVerificationCode);

    boolean existsByEmail(String email);
}