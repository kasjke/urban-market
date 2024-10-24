package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.dto.request.ProductRequestDto;
import com.example.urbanmarket.dto.response.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class ProductMapper {
    public ProductResponseDto toResponseDto(ProductEntity entity){
        return null;
    }

    public ProductEntity toEntity(ProductResponseDto dto){
        return null;
    }

    public ProductEntity toEntity(ProductRequestDto dto){
        return null;
    }


    public List<ProductResponseDto> toResponseDtoList(List<ProductEntity> entities){
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
