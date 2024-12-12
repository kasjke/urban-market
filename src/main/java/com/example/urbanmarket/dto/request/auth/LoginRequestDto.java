package com.example.urbanmarket.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto (@NotBlank @Email @Schema(example = "string@gmail.com") String email,
                               @NotBlank @Size(min = 8, max = 100) String password){
}