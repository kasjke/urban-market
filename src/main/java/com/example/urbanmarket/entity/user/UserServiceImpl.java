package com.example.urbanmarket.entity.user;

import com.example.urbanmarket.dto.request.UserRequestDto;
import com.example.urbanmarket.dto.response.UserResponseDto;
import com.example.urbanmarket.exception.exceptions.CustomAlreadyExistException;
import com.example.urbanmarket.exception.exceptions.CustomNotFoundException;
import com.example.urbanmarket.exception.LogEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final String OBJECT_NAME = "User";

    public UserResponseDto create(UserRequestDto request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomAlreadyExistException(OBJECT_NAME, "email", request.email());
        }

        UserEntity userEntity = userMapper.toEntity(request);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        log.info("{}: {} (Id: {}) was created", LogEnum.SERVICE, OBJECT_NAME, savedUserEntity.getId());
        return userMapper.toResponse(savedUserEntity);
    }

    public UserResponseDto getById(String id) {
        UserEntity userEntity = findById(id);

        log.info("{}: {} (Id: {}) was found", LogEnum.SERVICE, OBJECT_NAME, id);
        return userMapper.toResponse(userEntity);
    }

    public List<UserResponseDto> getAll() {
        List<UserResponseDto> users = userMapper.toResponseDtoList(userRepository.findAll());

        log.info("{}: all {} were obtained", LogEnum.SERVICE, OBJECT_NAME);
        return users;
    }

    public UserResponseDto update(String id, UserRequestDto request) {
        UserEntity userEntity = findById(id);

        userMapper.updateUserFromDto(request, userEntity);
        UserEntity updatedUserEntity = userRepository.save(userEntity);

        log.info("{}: {} (Id: {}) was updated", LogEnum.SERVICE, OBJECT_NAME, id);
        return userMapper.toResponse(updatedUserEntity);
    }

    public void delete(String id) {
        userRepository.deleteById(id);

        log.info("{}: {} (Id: {}) was deleted", LogEnum.SERVICE, OBJECT_NAME, id);
    }

    public UserEntity findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }
}