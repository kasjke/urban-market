package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.dto.request.ProductRequestDto;
import com.example.urbanmarket.dto.response.ProductResponseDto;
import com.example.urbanmarket.exception.LogEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private static final String OBJECT_NAME = "Product";

    private final ProductRepository repository;

    private final ProductMapper mapper;

    @Override
    public ProductResponseDto create(ProductRequestDto productDto) {
        ProductEntity entity = mapper.toEntity(productDto);
        entity = repository.save(entity);

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
        log.info("{}: " + OBJECT_NAME + " (id: {}) was updated", LogEnum.SERVICE, id);
        return mapper.toResponseDto(entity);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
        log.info("{}: " + OBJECT_NAME + " (id: {}) was deleted", LogEnum.SERVICE, id);
    }

    private ProductEntity findById(String id){
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }
}