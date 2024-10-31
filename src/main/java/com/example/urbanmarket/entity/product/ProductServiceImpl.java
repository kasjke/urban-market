package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.dto.request.product.ProductInCartRequestDto;
import com.example.urbanmarket.dto.request.product.ProductRequestDto;
import com.example.urbanmarket.dto.response.product.ProductInCartOrderResponseDto;
import com.example.urbanmarket.dto.response.product.ProductResponseDto;

import com.example.urbanmarket.entity.shop.ShopServiceImpl;
import com.example.urbanmarket.exception.LogEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private static final String OBJECT_NAME = "Product";

    private final ShopServiceImpl shopService;

    private final ProductRepository repository;

    private final ProductMapper mapper;

    @Override
    public ProductResponseDto create(ProductRequestDto productDto) {
        ProductEntity entity = mapper.toEntity(productDto);
        entity = repository.save(entity);
        shopService.addProductToShop(entity);

        log.info("{}: " + OBJECT_NAME + " (Id: {}) was created", LogEnum.SERVICE, entity.getId());
        return mapper.toResponseDto(entity);
    }

    @Override
    public ProductResponseDto getById(String id) {
        ProductEntity entity = findById(id);

        log.info("{}: " + OBJECT_NAME + " (Id: {}) was found", LogEnum.SERVICE, id);
        return mapper.toResponseDto(entity);
    }

    @Override
    public List<ProductResponseDto> getAll() {
        List<ProductEntity> entities = repository.findAll();

        log.info("{}: all " + OBJECT_NAME + "were obtained", LogEnum.SERVICE);
        return mapper.toResponseDtoList(entities);
    }

    @Override
    public ProductResponseDto update(String id, ProductRequestDto productDto) {
        ProductEntity fromDb = findById(id);
        ProductEntity entity = mapper.toEntity(productDto);
        entity.setId(fromDb.getId());

        repository.save(entity);
        shopService.addProductToShop(entity);
        log.info("{}: " + OBJECT_NAME + " (id: {}) was updated", LogEnum.SERVICE, id);
        return mapper.toResponseDto(entity);
    }

    @Override
    public void delete(String id) {
        shopService.removeProductFromShop(findById(id));
        repository.deleteById(id);

        log.info("{}: " + OBJECT_NAME + " (id: {}) was deleted", LogEnum.SERVICE, id);
    }

    public boolean existById(String id){
        return repository.existsById(id);
    }

    public ProductEntity findById(String id){
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<ProductEntity> findByIds(List<String> ids){
        return ids.stream().map(this::findById).collect(Collectors.toList());
    }

    public List<ProductInCartOrderResponseDto> getCartOrderResponseFigures(List<ProductInCartRequestDto> products) {
        return products
                .stream()
                .map(productDto -> {
                    ProductEntity product = findById(productDto.id());
                    return new ProductInCartOrderResponseDto(
                            product.getId(),
                            product.getName(),
                            product.getImages().get(0),
                            productDto.amount(),
                            product.getCurrentPrice()
                    );
                })
                .toList();
    }
}