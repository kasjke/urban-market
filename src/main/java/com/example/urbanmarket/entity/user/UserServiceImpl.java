package com.example.urbanmarket.entity.user;

import com.example.urbanmarket.dto.request.UserRequestDto;
import com.example.urbanmarket.dto.response.UserResponseDto;
import com.example.urbanmarket.exception.CustomAlreadyExistException;
import com.example.urbanmarket.exception.CustomNotFoundException;
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
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));

        log.info("{}: {} (Id: {}) was found", LogEnum.SERVICE, OBJECT_NAME, id);
        return userMapper.toResponse(userEntity);
    }

    public List<UserResponseDto> getAll() {
        List<UserResponseDto> users = userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();

        log.info("{}: all {} were obtained", LogEnum.SERVICE, OBJECT_NAME);
        return users;
    }

    public UserResponseDto update(String id, UserRequestDto request) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));

        userMapper.updateUserFromDto(request, userEntity);
        UserEntity updatedUserEntity = userRepository.save(userEntity);

        log.info("{}: {} (Id: {}) was updated", LogEnum.SERVICE, OBJECT_NAME, id);
        return userMapper.toResponse(updatedUserEntity);
    }

    public void delete(String id) {
        if (!userRepository.existsById(id)) {
            throw new CustomNotFoundException(OBJECT_NAME, id);
        }
        userRepository.deleteById(id);

        log.info("{}: {} (Id: {}) was deleted", LogEnum.SERVICE, OBJECT_NAME, id);
    }
}
