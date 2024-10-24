package com.example.urbanmarket.mapper;

import com.example.urbanmarket.dto.request.UserRequest;
import com.example.urbanmarket.dto.response.UserResponse;
import com.example.urbanmarket.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserRequest request, @MappingTarget User user);
}