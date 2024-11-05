package com.example.urbanmarket.entity.shop;

import com.example.urbanmarket.dto.request.ShopRequestDto;
import com.example.urbanmarket.dto.response.ShopResponseDto;
import com.example.urbanmarket.entity.product.ProductMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class ShopMapper {
    private final ProductMapper productMapper;

    public ShopResponseDto toResponseDto(ShopEntity entity){
        return new ShopResponseDto(entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getLogo(),
                productMapper.toResponseDtoList(entity.getProducts()));
    }

    public ShopEntity toEntity(ShopResponseDto dto){
        return new ShopEntity(dto.id(),
                dto.name(),
                dto.description(),
                dto.logo(),
                productMapper.toEntityList(dto.products()),
                new Date());
    }
    public ShopEntity toEntity(ShopRequestDto dto){
        return new ShopEntity(dto.name(),
                dto.description(),
                dto.logo(),
                null);
    }

    public List<ShopResponseDto> toResponseDtoList(List<ShopEntity> entities){
        return entities == null ? null : entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ShopEntity> toEntityListFromResponse (List<ShopResponseDto> dtos){
        return dtos == null ? null : dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}