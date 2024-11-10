package com.example.urbanmarket.entity.user;

import com.example.urbanmarket.config.CustomMapperConfig;
import com.example.urbanmarket.dto.request.UserRequestDto;
import com.example.urbanmarket.dto.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", config = CustomMapperConfig.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(UserRequestDto request);

    UserResponseDto toResponse(UserEntity userEntity);

    List<UserResponseDto> toResponseDtoList(List<UserEntity> entities);
    List<UserEntity> toEntityList(List<UserResponseDto> dtos);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserRequestDto request, @MappingTarget UserEntity userEntity);
}