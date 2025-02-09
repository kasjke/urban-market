package com.example.urbanmarket.dto.request;

import com.example.urbanmarket.entity.shop.contacts.ContactInfo;


public record ShopRequestDto( String name, String description,
                             String logo, ContactInfo contacts)  {
}
