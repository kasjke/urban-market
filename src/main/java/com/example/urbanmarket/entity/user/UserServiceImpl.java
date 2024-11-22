package com.example.urbanmarket.entity.user;

import com.example.urbanmarket.dto.request.UserRequestDto;
import com.example.urbanmarket.dto.request.auth.SignupRequestDto;
import com.example.urbanmarket.dto.response.UserResponseDto;
import com.example.urbanmarket.exception.exceptions.general.CustomAlreadyExistException;
import com.example.urbanmarket.exception.exceptions.general.CustomNotFoundException;
import com.example.urbanmarket.exception.LogEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final String OBJECT_NAME = "User";

    @Override
    public UserResponseDto create(SignupRequestDto request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomAlreadyExistException(OBJECT_NAME, "email", request.email());
        }

        UserEntity userEntity = userMapper.toEntity(request);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        log.info("{}: {} (Id: {}) was created", LogEnum.SERVICE, OBJECT_NAME, savedUserEntity.getId());
        return userMapper.toResponse(savedUserEntity);
    }

    @Override
    public UserResponseDto getById(String id) {
        UserEntity userEntity = findById(id);

        log.info("{}: {} (Id: {}) was found", LogEnum.SERVICE, OBJECT_NAME, id);
        return userMapper.toResponse(userEntity);
    }

    @Override
    public List<UserResponseDto> getAll() {
        List<UserResponseDto> users = userMapper.toResponseDtoList(userRepository.findAll());

        log.info("{}: all {} were obtained", LogEnum.SERVICE, OBJECT_NAME);
        return users;
    }

    @Override
    public UserResponseDto update(String id, UserRequestDto request) {
        UserEntity userEntity = findById(id);

        userMapper.updateUserFromDto(request, userEntity);
        UserEntity updatedUserEntity = userRepository.save(userEntity);

        log.info("{}: {} (Id: {}) was updated", LogEnum.SERVICE, OBJECT_NAME, id);
        return userMapper.toResponse(updatedUserEntity);
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);

        log.info("{}: {} (Id: {}) was deleted", LogEnum.SERVICE, OBJECT_NAME, id);
    }

    public UserEntity findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }

    public UserEntity findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new CustomNotFoundException(OBJECT_NAME, "email", email));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user;
        try {
            user = findByEmail(email);
        } catch (CustomNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}