package com.example.urbanmarket.dto.request;

import java.util.List;

public record ProductRequestDto(String name, String description,
                                int currentPrice, int oldPrice, int amount,
                                List<String> images, String shopId) {
}
