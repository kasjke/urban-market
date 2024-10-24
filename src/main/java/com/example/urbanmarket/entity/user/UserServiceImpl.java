package com.example.urbanmarket.entity.user;

import com.example.urbanmarket.dto.request.UserRequestDto;
import com.example.urbanmarket.dto.response.UserResponseDto;
import com.example.urbanmarket.exception.userExceptions.UserAlreadyExistsException;
import com.example.urbanmarket.exception.userExceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto createUser(UserRequestDto request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(request.email());
        }

        UserEntity userEntity = userMapper.toEntity(request);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return userMapper.toResponse(savedUserEntity);
    }

    public UserResponseDto getUserById(String id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return userMapper.toResponse(userEntity);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponseDto updateUser(String id, UserRequestDto request) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        userMapper.updateUserFromDto(request, userEntity);
        UserEntity updatedUserEntity = userRepository.save(userEntity);
        return userMapper.toResponse(updatedUserEntity);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }
}