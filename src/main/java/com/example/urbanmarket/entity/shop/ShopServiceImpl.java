package com.example.urbanmarket.entity.shop;

import com.example.urbanmarket.dto.request.ShopRequestDto;
import com.example.urbanmarket.dto.response.ShopResponseDto;
import com.example.urbanmarket.entity.product.ProductEntity;
import com.example.urbanmarket.exception.LogEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService{
    private static final String OBJECT_NAME = "Shop";

    private final ShopRepository repository;

    private final ShopMapper mapper;

    @Override
    public ShopResponseDto create(ShopRequestDto requestDto) {
        ShopEntity entity = mapper.toEntity(requestDto);
        entity = repository.save(entity);

        log.info("{}: " + OBJECT_NAME + " (Id: {}) was created", LogEnum.SERVICE, entity.getId());
        return mapper.toResponseDto(entity);
    }

    @Override
    public ShopResponseDto getById(String id) {
        ShopEntity entity = findById(id);

        log.info("{}: " + OBJECT_NAME + " (Id: {}) was found", LogEnum.SERVICE, id);
        return mapper.toResponseDto(entity);
    }

    @Override
    public List<ShopResponseDto> getAll() {
        List<ShopEntity> entities = repository.findAll();

        log.info("{}: all " + OBJECT_NAME + "were obtained", LogEnum.SERVICE);
        return mapper.toResponseDtoList(entities);
    }

    @Override
    public ShopResponseDto update(String id, ShopRequestDto requestDto) {
        ShopEntity fromDb = findById(id);
        ShopEntity entity = mapper.toEntity(requestDto);
        entity.setId(fromDb.getId());

        repository.save(entity);
        log.info("{}: " + OBJECT_NAME + " (id: {}) was updated", LogEnum.SERVICE, id);
        return mapper.toResponseDto(entity);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
        log.info("{}: " + OBJECT_NAME + " (id: {}) was deleted", LogEnum.SERVICE, id);
    }

    public boolean existById(String id){
        return repository.existsById(id);
    }

    public ShopEntity findById(String id){
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }

    public void addProductToShop(ProductEntity product){
        ShopEntity shop = findById(product.getShopId());
        shop.addProduct(product);
        repository.save(shop);
    }

    public void removeProductFromShop(ProductEntity product){
        ShopEntity shop = findById(product.getShopId());
        shop.removeProduct(product.getId());
        repository.save(shop);
    }
}
