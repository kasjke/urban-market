package com.example.urbanmarket.entity.cart;

import com.example.urbanmarket.dto.request.CartRequestDto;
import com.example.urbanmarket.dto.response.CartResponseDto;
import com.example.urbanmarket.dto.response.product.ProductInCartOrderResponseDto;
import com.example.urbanmarket.entity.product.ProductServiceImpl;
import com.example.urbanmarket.entity.user.UserEntity;
import com.example.urbanmarket.exception.LogEnum;
import com.example.urbanmarket.exception.exceptions.CustomBadRequestException;
import com.example.urbanmarket.exception.exceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;

    private final CartMapper cartMapper;
    private final ProductServiceImpl productService;
//    private final UserServiceImpl userService;
//    private final PromoCodeServiceImpl promoCodeService;

    private static final String OBJECT_NAME = "Cart";

    @Override
    public List<CartResponseDto> getAll() {
        List<CartEntity> cartList = cartRepository.findAll();
        log.info("{}: All " + OBJECT_NAME + "s retrieved from db", LogEnum.SERVICE);
        return cartMapper.toResponseDtoList(cartList);
    }

    @Override
    public CartResponseDto getById(String id) {
        CartEntity cart = findById(id);
        log.info("{}: " + OBJECT_NAME + " retrieved from db by id {}", LogEnum.SERVICE, id);
        return cartMapper.toResponseDto(cart);
    }

    @Override
    public CartResponseDto create(CartRequestDto cartDto) {
        /*
        GETTING USER
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userService.findByEmail(email);
         */

        UserEntity currentUser = new UserEntity();


        //VALIDATION CHECK | NOT REAL IDS
        List<ProductInCartOrderResponseDto> products = productService.getCartOrderResponseProducts(cartDto.products());
        //products.removeIf(elem -> !figureService.existById(elem.figureId())); - This variant isn't working (IDK why...)
        for (ProductInCartOrderResponseDto elem : products){
            if (!productService.existById(elem.id())){
                products.remove(elem);
            }
        }

        if (products.isEmpty()) {
            throw new RuntimeException("This request is not valid");
        }
        if (cartRepository.existsByUser(currentUser)) {
            CartEntity cart = cartRepository.findByUser(currentUser);
            return cartMapper.toResponseDto(addProducts(cart, products));
        }

        CartEntity newCart = new CartEntity(currentUser, products, getDiscount(cartDto.promoCode()));
        CartEntity savedCart = cartRepository.save(newCart);

        log.info("{}: " + OBJECT_NAME + " (Id: {}) was created", LogEnum.SERVICE, savedCart.getId());
        return cartMapper.toResponseDto(savedCart);
    }

    @Override
    public CartResponseDto update(String id, CartRequestDto cartDto) {
        CartEntity cart = findById(id);
        List<ProductInCartOrderResponseDto> products = productService.getCartOrderResponseProducts(cartDto.products());

        if (products.isEmpty()) {
            throw new CustomBadRequestException();
        }

        cart.setProducts(products);
        cart.setTotalPrice(getDiscount(cartDto.promoCode()));

        CartEntity updatedCart = cartRepository.save(cart);
        log.info("{}: " + OBJECT_NAME + " (Id: {}) updated product list and total price throughout update method", LogEnum.SERVICE, id);
        return cartMapper.toResponseDto(updatedCart);
    }

    @Override
    public void delete(String id) {
        CartEntity cart = findById(id);
        cartRepository.delete(cart);
        log.info("{}: " + OBJECT_NAME + " (id: {}) deleted", LogEnum.SERVICE, id);
    }

    public CartEntity findById(String id) {
        return cartRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }

    private int getDiscount(String promoCode){
        int discount = 0;
//        if (promoCode==null||promoCode.isBlank()){
//            discount = promoCodeService.getByCode(promoCode).discount();
//        }

        return discount;
    }

    private CartEntity addProducts(CartEntity cart, List<ProductInCartOrderResponseDto> products) {
        List<ProductInCartOrderResponseDto> productsInCart = new ArrayList<>(cart.getProducts());

        Map<String, ProductInCartOrderResponseDto> figureMap = productsInCart.stream()
                .collect(Collectors.toMap(ProductInCartOrderResponseDto::id, Function.identity()));

        products.forEach(elem -> figureMap.put(elem.id(), elem));

        productsInCart.clear();
        productsInCart.addAll(figureMap.values());

        cart.setProducts(productsInCart);

        CartEntity savedCart = cartRepository.save(cart);
        log.info("{}: " + OBJECT_NAME + " (Id: {}) updated product list and total price throughout create method", LogEnum.SERVICE, savedCart.getId());
        return savedCart;
    }
}
