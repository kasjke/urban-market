package com.example.urbanmarket.entity.user;

import com.example.urbanmarket.config.mail.service.EmailService;
import com.example.urbanmarket.dto.request.UserRequestDto;
import com.example.urbanmarket.dto.request.auth.SignupRequestDto;
import com.example.urbanmarket.dto.response.UserResponseDto;
import com.example.urbanmarket.enums.Role;
import com.example.urbanmarket.exception.LogEnum;
import com.example.urbanmarket.exception.exceptions.general.CustomAlreadyExistException;
import com.example.urbanmarket.exception.exceptions.general.CustomNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private static final String OBJECT_NAME = "User";

    private PasswordEncoder passwordEncoder;
    @Autowired
    public void passwordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto create(SignupRequestDto request) {

        String email = request.email();
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomAlreadyExistException(OBJECT_NAME, "email", email);
        }

        UserEntity user = new UserEntity(request.firstName(), request.lastName(), email, request.password());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(request.password()));
        UserEntity savedUserEntity = userRepository.save(user);

        String helloMesSubject = "Welcome from Urban Zen!";
        String helloMesText = "Hello from Urban Zen marketplace, happy to see you on our marketplace!";

        emailService.sendLetter(email, helloMesSubject, helloMesText);
        emailService.sendVerificationEmailLetter(email, savedUserEntity.getEmailVerificationCode());
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

        String email = request.email();
        String password = request.password();

        if (!email.equals(userEntity.getEmail())) {
            if (existByEmail(email)){
                throw new CustomAlreadyExistException(OBJECT_NAME, "Email", email);
            }
            userEntity.setEmail(email);
            userEntity.setEmailVerified(false);
            userEntity.setEmailVerificationCode(UUID.randomUUID().toString().substring(0, 6));
        }
        if (!passwordEncoder.matches(password, userEntity.getPassword())){
            userEntity.setPassword(passwordEncoder.encode(password));
            userEntity.setPasswordVerified(false);
            userEntity.setPasswordVerificationCode(UUID.randomUUID().toString().substring(0, 6));
        }

        userMapper.updateUserFromDto(request, userEntity);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity updatedUserEntity = userRepository.save(userEntity);

        log.info("{}: {} (Id: {}) was updated", LogEnum.SERVICE, OBJECT_NAME, id);
        return userMapper.toResponse(updatedUserEntity);
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);

        log.info("{}: {} (Id: {}) was deleted", LogEnum.SERVICE, OBJECT_NAME, id);
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


    //VERIFICATION MESSAGES
    public UserResponseDto sendEmilVerifMessage(String email){
        UserEntity user = findByEmail(email);

        if (user.getEmailVerificationCode()==null){
            user.setEmailVerificationCode(UUID.randomUUID().toString().substring(0, 6));
            userRepository.save(user);
        }

        emailService.sendVerificationEmailLetter(email, user.getEmailVerificationCode());
        return userMapper.toResponse(user);
    }

    public UserResponseDto sendPasswordVerifMessage(String email){
        UserEntity user = findByEmail(email);

        if (user.getPasswordVerificationCode()==null){
            user.setPasswordVerificationCode(UUID.randomUUID().toString().substring(0, 6));
            userRepository.save(user);
        }

        emailService.sendVerificationPasswordLetter(email, user.getPasswordVerificationCode());
        return userMapper.toResponse(user);
    }


    //CONFIRMATION
    public UserResponseDto confirmEmail(String emailVerificationCode) {
        UserEntity user = findByEmailVerificationCode(emailVerificationCode);
        user.setEmailVerified(true);
        user.setEmailVerificationCode(null);
        log.info("{}: " + OBJECT_NAME + "'s (id: {}) email has been confirmed", LogEnum.SERVICE, user.getId());
        UserEntity savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public UserResponseDto confirmPassword(String passwordVerificationCode) {
        UserEntity user = findByPasswordVerificationCode(passwordVerificationCode);
        user.setPasswordVerified(true);
        user.setPasswordVerificationCode(null);
        log.info("{}: " + OBJECT_NAME + "'s (id: {}) password has been confirmed", LogEnum.SERVICE, user.getId());
        UserEntity savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }


    //EXIST BY
    public boolean existByEmail (String email){
        return userRepository.existsByEmail(email);
    }


    //FIND BY
    public UserEntity findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }

    public UserEntity findByEmail (String email) {
        log.info("{}: request on retrieving " + OBJECT_NAME + " by email {} was sent", LogEnum.SERVICE, email);
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, email));
    }

    public UserEntity findByEmailVerificationCode(String verificationCode) {
        log.info("{}: request on retrieving " + OBJECT_NAME + " by email verification code {} was sent", LogEnum.SERVICE, verificationCode);
        return userRepository.findByEmailVerificationCode(verificationCode).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME));
    }

    public UserEntity findByPasswordVerificationCode(String verificationCode) {
        log.info("{}: request on retrieving " + OBJECT_NAME + " by password verification code {} was sent", LogEnum.SERVICE, verificationCode);
        return userRepository.findByPasswordVerificationCode(verificationCode).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME));
    }
}