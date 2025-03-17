package com.example.urbanmarket.dto.request;

import jakarta.validation.constraints.Positive;


public record RequestUpdatePriceDto(@Positive(message = "price need to be positive") int newPrice) {

}