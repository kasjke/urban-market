package com.example.urbanmarket.entity.cart;

import com.example.urbanmarket.dto.response.CartResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class CartMapper {
    public CartResponseDto toResponseDto(CartEntity entity){
        return new CartResponseDto(
                entity.getId(),
                entity.getProducts(),
                entity.getTotalPrice()
        );
    }

    public List<CartResponseDto> toDtoList (List<CartEntity> entities){
        return entities == null ? null : entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
