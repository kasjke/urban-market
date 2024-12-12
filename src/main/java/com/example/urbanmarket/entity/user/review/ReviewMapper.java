package com.example.urbanmarket.entity.user.review;


import com.example.urbanmarket.config.CustomMapperConfig;
import com.example.urbanmarket.dto.request.ReviewRequestDto;
import com.example.urbanmarket.dto.response.ReviewResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", config = CustomMapperConfig.class)
public interface ReviewMapper {

    ReviewEntity toEntity(ReviewRequestDto dto);
    ReviewResponseDto toResponseDto(ReviewEntity review);

    List<ReviewResponseDto> toResponseDtoList(List<ReviewEntity> entities);
    List<ReviewEntity> toEntityList(List<ReviewResponseDto> dtos);
}
