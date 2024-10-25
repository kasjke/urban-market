package com.example.urbanmarket.dto.response;

import java.util.List;

public record ProductResponseDto(String id, String name, String description,
                                 int currentPrice, int oldPrice, int amount,
                                 List<String> images, int purchaseCount, String shopId) {
}
