package com.example.urbanmarket.entity.user;

import com.example.urbanmarket.config.CustomMapperConfig;
import com.example.urbanmarket.dto.request.UserRequestDto;
import com.example.urbanmarket.dto.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", config = CustomMapperConfig.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(UserRequestDto request);

    UserResponseDto toResponse(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserRequestDto request, @MappingTarget UserEntity userEntity);
}