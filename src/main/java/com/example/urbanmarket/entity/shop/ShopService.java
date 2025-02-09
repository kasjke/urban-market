package com.example.urbanmarket.entity.shop;

import com.example.urbanmarket.dto.request.ShopRequestDto;
import com.example.urbanmarket.dto.response.ShopBannerResponseDto;
import com.example.urbanmarket.dto.response.ShopCreateResponseDto;
import com.example.urbanmarket.dto.response.ShopResponseDto;
import com.example.urbanmarket.entity.shop.contacts.ContactInfo;

import java.util.List;

public interface ShopService {
    ShopCreateResponseDto create(ShopRequestDto requestDto);
    ShopBannerResponseDto getById(String id);
    List<ShopResponseDto> getAll();
    ShopResponseDto update(String id, ShopRequestDto requestDto);
    ContactInfo updateContactInfo(String id, ContactInfo contacts);
    void delete(String id);

    ShopResponseDto getShopsByIdWithProducts(String id);
}
