package com.example.urbanmarket.entity.cart;

import com.example.urbanmarket.dto.response.CartResponseDto;
import com.example.urbanmarket.dto.response.product.ProductInCartOrderResponseDto;
import com.example.urbanmarket.entity.product.ProductServiceImpl;
import com.example.urbanmarket.entity.user.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    @Mock
    CartRepository cartRepository;
    @Mock
    CartMapper cartMapper;
    @Mock
    ProductServiceImpl productService;
    @InjectMocks
    CartServiceImpl cartServiceImpl;

    List<CartResponseDto> carts;

    @BeforeEach
    void setUp(){
        UserEntity user1 = new UserEntity("TestName", "TestLastName", "test@mail.com", "some_test_pas");
        List<ProductInCartOrderResponseDto> products1 = Collections.singletonList(new ProductInCartOrderResponseDto("1p", "prodName", "prodIm", 2, 40));

        UserEntity user2 = new UserEntity("AnotherTestName", "AnotherTestLastName", "anothertest@mail.com", "another_some_test_pas");
        List<ProductInCartOrderResponseDto> products2 = Collections.singletonList(new ProductInCartOrderResponseDto("2p", "prodNameSecond", "prodImSecond", 1, 20));

        List<CartEntity> cartsList = Arrays.asList(
                new CartEntity("1", products1, user1, 80),
                new CartEntity("2", products2, user2, 40)
        );

        carts = cartMapper.toResponseDtoList(cartsList);

        Mockito.when(cartRepository.findAll()).thenReturn(cartsList);
    }

    @Test
    void getAll_test(){
        List<CartResponseDto> all = cartServiceImpl.getAll();

        Assertions.assertEquals(carts, all);
    }

    @Test
    void getById_test(){
        CartResponseDto byId = cartServiceImpl.getById("1");

        Assertions.assertEquals(carts.get(0), byId);
    }
}