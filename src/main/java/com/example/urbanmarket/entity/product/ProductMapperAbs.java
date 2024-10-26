package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.dto.request.ProductRequestDto;
import com.example.urbanmarket.dto.response.ProductResponseDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ProductMapperAbs {

    //@Mapping(target = "id", ignore = true)
    abstract ProductEntity toEntity(ProductRequestDto dto);
    abstract ProductEntity toEntity(ProductResponseDto dto);

    abstract ProductResponseDto toResponseDto(ProductEntity entity);

    public List<ProductResponseDto> toResponseDtoList(java.util.List<ProductEntity> entities){
        return entities == null ? null : entities
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<ProductEntity> toEntityList(List<ProductResponseDto> dtos){
        return dtos == null ? null : dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}