package com.example.urbanmarket.controller;

import com.example.urbanmarket.dto.request.product.ProductRequestDto;
import com.example.urbanmarket.dto.response.ProductResponseDto;
import com.example.urbanmarket.entity.product.ProductService;
import com.example.urbanmarket.exception.LogEnum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private static final String URI_WITH_ID = "/{id}";
    //private static final String SEC_REC = "BearerAuth";
    private static final String OBJECT_NAME = "Product";

    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //@SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Add new Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new Product",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))})
    })
    public ProductResponseDto create(@Valid @RequestBody ProductRequestDto request) {
        //accessValidator.isAdmin();

        ProductResponseDto product = service.create(request);

        log.info("{}: {} (id: {}) has been added", LogEnum.SERVICE, OBJECT_NAME, product.id());
        return product;
    }

    @GetMapping
    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class)))}
            )
    })
    public List<ProductResponseDto> getAll() {
        List<ProductResponseDto> all = service.getAll();

        log.info("{}: {}s have been retrieved", LogEnum.CONTROLLER, OBJECT_NAME);
        return all;
    }

    @GetMapping(URI_WITH_ID)
    //@SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Get product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product received",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class)) })

    })
    public ProductResponseDto getById(@PathVariable String id) {
        ProductResponseDto product = service.getById(id);
        log.info("{}: {} (id: {}) has been retrieved", LogEnum.SERVICE, OBJECT_NAME, product.id());
        return product;
    }

    @PutMapping(URI_WITH_ID)
    //@SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Update product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))})
    })
    public ProductResponseDto update(@PathVariable String id, @Valid @RequestBody ProductRequestDto requestDto) {
        //accessValidator.isAdmin();

        ProductResponseDto product = service.update(id, requestDto);
        log.info("{}: {} (id: {}) has been updated", LogEnum.CONTROLLER, OBJECT_NAME, id);
        return product;
    }

    @DeleteMapping(URI_WITH_ID)
    //@SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Delete product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class)) }) })
    public void delete(@PathVariable String id){
        //accessValidator.isAdmin();

        service.delete(id);
        log.info("{}: {} (id: {}) has been deleted", LogEnum.CONTROLLER, OBJECT_NAME, id);
    }
}
