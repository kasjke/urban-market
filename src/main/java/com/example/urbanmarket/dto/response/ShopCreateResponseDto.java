package com.example.urbanmarket.dto.response;

import com.example.urbanmarket.entity.shop.contacts.ContactInfo;


public record ShopCreateResponseDto(String id, String name, String description,
                                    String logo, ContactInfo contacts) {
}
