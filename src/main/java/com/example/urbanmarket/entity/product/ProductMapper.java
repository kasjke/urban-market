package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.config.CustomMapperConfig;
import com.example.urbanmarket.dto.request.ProductAddDto;
import com.example.urbanmarket.dto.request.product.ProductRequestDto;
import com.example.urbanmarket.dto.response.product.ProductResponseDto;
import com.example.urbanmarket.dto.response.product.ProductResponseYouMayAlsoDto;
import com.example.urbanmarket.entity.user.review.ReviewMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", config = CustomMapperConfig.class, uses = ReviewMapper.class)
public interface ProductMapper {

    ProductEntity toEntity(ProductRequestDto dto);

    ProductEntity toAddEntity(ProductAddDto productAddDto);

    ProductResponseDto toResponseDto(ProductEntity entity);

    ProductAddDto toResponseAddDto(ProductEntity entity);

    List<ProductResponseDto> toResponseDtoList(List<ProductEntity> entities);

    List<ProductEntity> toEntityList(List<ProductResponseDto> dtos);

    List<ProductResponseYouMayAlsoDto> toResponseYouMayAlsoDtoList(List<ProductEntity> similarProducts);
}