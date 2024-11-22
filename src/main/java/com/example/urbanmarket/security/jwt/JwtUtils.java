package com.example.urbanmarket.security.jwt;

import com.example.urbanmarket.entity.user.UserEntity;

public class JwtUtils {
    public String generateToken(UserEntity user){
        return "some jwt token";
    }
}
