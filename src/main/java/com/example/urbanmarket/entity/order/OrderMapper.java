package com.example.urbanmarket.entity.order;


import com.example.urbanmarket.config.CustomMapperConfig;
import com.example.urbanmarket.dto.request.OrderRequestDto;
import com.example.urbanmarket.dto.response.OrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", config = CustomMapperConfig.class)
public interface OrderMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrderEntity toEntity(OrderRequestDto orderRequestDto);

    OrderResponseDto toResponseDto(OrderEntity orderEntity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateOrderFromDto(OrderRequestDto orderRequestDto, @MappingTarget OrderEntity orderEntity);

    List<OrderResponseDto> toResponseDtoList(List<OrderEntity> entities);

    List<OrderEntity> toEntityList(List<OrderResponseDto> dtos);
}
