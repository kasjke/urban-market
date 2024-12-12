package com.example.urbanmarket;

import com.example.urbanmarket.dto.request.UserRequestDto;
import com.example.urbanmarket.dto.response.UserResponseDto;
import com.example.urbanmarket.entity.user.UserEntity;
import com.example.urbanmarket.entity.user.UserMapper;
import com.example.urbanmarket.entity.user.UserRepository;
import com.example.urbanmarket.entity.user.UserServiceImpl;
import com.example.urbanmarket.exception.exceptions.CustomAlreadyExistException;
import com.example.urbanmarket.exception.exceptions.CustomNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldSaveUser_WhenEmailDoesNotExist() {
        UserRequestDto requestDto = new UserRequestDto("Name", "Second Name", "test@example.com", "+1344444", "35555555");
        UserEntity userEntity = new UserEntity();
        UserEntity savedEntity = new UserEntity();
        UserResponseDto responseDto = new UserResponseDto("1", "Name", "Second Name", "test@example.com", "+1344444");

        when(userRepository.existsByEmail(requestDto.email())).thenReturn(false);
        when(userMapper.toEntity(requestDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedEntity);
        when(userMapper.toResponse(savedEntity)).thenReturn(responseDto);

        UserResponseDto result = userService.create(requestDto);

        assertEquals(responseDto, result);
        verify(userRepository).existsByEmail(requestDto.email());
        verify(userRepository).save(userEntity);
        verify(userMapper).toEntity(requestDto);
        verify(userMapper).toResponse(savedEntity);
    }

    @Test
    void create_ShouldThrowException_WhenEmailExists() {
        UserRequestDto requestDto = new UserRequestDto("Name", "Second Name", "test@example.com", "+1344444", "35555555");
        when(userRepository.existsByEmail(requestDto.email())).thenReturn(true);

        assertThrows(CustomAlreadyExistException.class, () -> userService.create(requestDto));
        verify(userRepository).existsByEmail(requestDto.email());
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    void getById_ShouldReturnUser_WhenUserExists() {
        String id = "1";
        UserEntity userEntity = new UserEntity();
        UserResponseDto responseDto = new UserResponseDto("1", "Name", "Second Name", "test@example.com", "+1344444");

        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(userMapper.toResponse(userEntity)).thenReturn(responseDto);

        UserResponseDto result = userService.getById(id);

        assertEquals(responseDto, result);
        verify(userRepository).findById(id);
        verify(userMapper).toResponse(userEntity);
    }

    @Test
    void getById_ShouldThrowExceptionWithCorrectMessage_WhenUserDoesNotExist() {
        String id = "1";
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> userService.getById(id));
        assertEquals("User with id/code: " + id + " not found.", exception.getMessage());
    }
    @Test
    void getById_ShouldCallRepositoryFindById_WhenUserDoesNotExist() {
        String id = "1";
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomNotFoundException.class, () -> userService.getById(id));
        verify(userRepository).findById(id);
    }


    @Test
    void delete_ShouldRemoveUser_WhenUserExists() {
        String id = "1";
        doNothing().when(userRepository).deleteById(id);

        userService.delete(id);

        verify(userRepository).deleteById(id);
    }
}
