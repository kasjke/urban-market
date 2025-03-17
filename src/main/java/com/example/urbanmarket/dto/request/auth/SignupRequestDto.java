package com.example.urbanmarket.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequestDto (@NotBlank @Size (min = 2, max = 50) String firstName,
                                @NotBlank @Size (min = 2, max = 50) String lastName,
                                @NotBlank @Email String email,
                                @NotBlank @Size(min = 8, max = 100) String password){
}