package com.example.urbanmarket.entity.cart;

import com.example.urbanmarket.dto.request.CartRequestDto;
import com.example.urbanmarket.dto.response.CartResponseDto;

import java.util.List;

public interface CartService {
    List<CartResponseDto> getAll();
    CartResponseDto getById(String id);
    CartResponseDto create(CartRequestDto cartRequestDto);
    CartResponseDto update(String id, CartRequestDto requestDto);
    void delete(String id);
}
