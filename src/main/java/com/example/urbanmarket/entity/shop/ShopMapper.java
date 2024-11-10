package com.example.urbanmarket.entity.shop;

import com.example.urbanmarket.config.CustomMapperConfig;
import com.example.urbanmarket.dto.request.ShopRequestDto;
import com.example.urbanmarket.dto.response.ShopResponseDto;
import com.example.urbanmarket.entity.product.ProductMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", config = CustomMapperConfig.class, uses = ProductMapper.class)
public interface ShopMapper {
    ShopEntity toEntity(ShopResponseDto dto);
    ShopEntity toEntity(ShopRequestDto dto);

    ShopResponseDto toResponseDto(ShopEntity entity);
    List<ShopResponseDto> toResponseDtoList(List<ShopEntity> entities);
}