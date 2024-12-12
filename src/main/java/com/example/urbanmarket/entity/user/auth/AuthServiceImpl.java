package com.example.urbanmarket.entity.user.auth;

import com.example.urbanmarket.dto.request.auth.LoginRequestDto;
import com.example.urbanmarket.dto.request.auth.SignupRequestDto;
import com.example.urbanmarket.dto.response.UserResponseDto;
import com.example.urbanmarket.entity.user.UserEntity;
import com.example.urbanmarket.entity.user.UserServiceImpl;
import com.example.urbanmarket.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public UserResponseDto signUp(SignupRequestDto signupRequestDto) {
        return userService.create(signupRequestDto);
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) throws Exception {
        UserEntity user = userService.findByEmail(loginRequestDto.email());

//        if (!user.isEmailVerified()) {
//            throw new UnverifiedAccountException(loginRequestDto.email());
//        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password())
            );
        } catch (AuthenticationException e) {
            throw new Exception("Authentication Exception", e);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.generateToken(user.getId(), user.getEmail(), user.getFirstName()+" "+user.getLastName());
    }
    /*

    @Override
    public UserResponseDto emailVerification(String emailVerificationCode) {
        return userService.confirmEmail(emailVerificationCode);
    }

    @Override
    public UserResponseDto passwordVerification(String passwordVerificationCode) {
        return userService.confirmPassword(passwordVerificationCode);
    }

    @Override
    public UserResponseDto sentEmailVerifMes(String email) {
        return userService.sendEmilVerifMessage(email);
    }

    @Override
    public UserResponseDto sentPasswordVerifMes(String email) {
        return userService.sendPasswordVerifMessage(email);
    }

    @Override
    public UserResponseDto resetPassword(LoginRequestDto loginRequestDto) {
        UserEntity user = userService.findByEmail(loginRequestDto.email());
        UserRequestDto userRequestDto = new UserRequestDto(user.getUsername(), user.getEmail(), loginRequestDto.getPassword(), user.getPhoneNumber());
        return userService.update(user.getId(), userRequestDto);
    }

     */
}