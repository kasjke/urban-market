package com.example.urbanmarket.controller;

import com.example.urbanmarket.dto.request.ProductAddDto;
import com.example.urbanmarket.dto.request.RequestUpdatePriceDto;
import com.example.urbanmarket.dto.request.product.ProductRequestDto;
import com.example.urbanmarket.dto.response.ResponseUpdatePriceDto;
import com.example.urbanmarket.dto.response.product.ProductResponseDto;
import com.example.urbanmarket.dto.response.product.ProductResponseYouMayAlsoDto;
import com.example.urbanmarket.entity.product.ProductService;
import com.example.urbanmarket.enums.Color;
import com.example.urbanmarket.enums.ProductSize;
import com.example.urbanmarket.exception.LogEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private static final String URI_WITH_ID = "/{id}";
    private static final String OBJECT_NAME = "Product";

    private final ProductService service;
    private final ObjectMapper objectMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add new Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new Product",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))})
    })
    public ProductResponseDto create(@Valid @RequestBody ProductRequestDto request) {
        ProductResponseDto product = service.create(request);
        log.info("{}: {} (id: {}) has been added", LogEnum.SERVICE, OBJECT_NAME, product.id());
        return product;
    }
    @PostMapping("/add")
    public String addProduct(
            @RequestParam("productRequestDto") String productRequestDto,
            @RequestParam("titleImage") MultipartFile titleImageFile,
            @RequestPart(value = "additionalImages", required = false) List<MultipartFile> additionalImageFiles
    ) {
        ProductAddDto bookDto;
        try {
            bookDto = objectMapper.readValue(productRequestDto, ProductAddDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format for productDto", e);
        }

        return service.addProduct(bookDto, titleImageFile,additionalImageFiles);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class)))}
            )
    })
    public List<ProductResponseDto> getAll() {
        List<ProductResponseDto> all = service.getAll();
        log.info("{}: {}s have been retrieved", LogEnum.CONTROLLER, OBJECT_NAME);
        return all;
    }

    @GetMapping(URI_WITH_ID)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product received",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))})
    })
    public ProductResponseDto getById(@PathVariable String id) {
        ProductResponseDto product = service.getById(id);
        log.info("{}: {} (id: {}) has been retrieved", LogEnum.SERVICE, OBJECT_NAME, product.id());
        return product;
    }

    @PutMapping(URI_WITH_ID)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Update product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))})
    })
    public ProductResponseDto update(@PathVariable String id, @Valid @RequestBody ProductRequestDto requestDto) {
        ProductResponseDto product = service.update(id, requestDto);
        log.info("{}: {} (id: {}) has been updated", LogEnum.CONTROLLER, OBJECT_NAME, id);
        return product;
    }

    @GetMapping("/{id}/similar-products")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get similar products by ID",
            description = "Retrieve a list of similar products based on the specified product ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of similar products retrieved",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProductResponseYouMayAlsoDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))})
    })
    public List<ProductResponseYouMayAlsoDto> getSimilarProducts(@PathVariable String id) {
        return service.findSimilarProducts(id);
    }

    @DeleteMapping(URI_WITH_ID)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Delete product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))})
    })
    public void delete(@PathVariable String id) {
        service.delete(id);
        log.info("{}: {} (id: {}) has been deleted", LogEnum.CONTROLLER, OBJECT_NAME, id);
    }

    @GetMapping("/new-arrivals")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get new arrivals")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of new arrival products",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class)))})
    })
    public List<ProductResponseDto> getNewArrivals(Pageable pageable) {
        List<ProductResponseDto> newArrivals = service.getNewArrivals();
        log.info("{}: Retrieved new arrivals, page size: {}", LogEnum.CONTROLLER, pageable.getPageSize());
        return newArrivals;
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponseDto>> getFilteredProducts(

            @Parameter(description = "Category name")
            @RequestParam(required = false) String categoryName,
            @Parameter(description = "Sort by creation time (ASC/DESC)")
            @RequestParam(required = false) String createdAt,
            @Parameter(description = "Sort by price (ASC/DESC)")
            @RequestParam(required = false) String price,
            @Parameter(description = "Minimum price value")
            @RequestParam(required = false) Integer priceMin,
            @Parameter(description = "Maximum price value")
            @RequestParam(required = false) Integer priceMax,
            @Parameter(description = "Color of the product")
            @RequestParam(required = false) Color color,
            @Parameter(description = "Size of the product")
            @RequestParam(required = false) ProductSize size
    ) {
        List<ProductResponseDto> products = service.getFilteredProducts(
                categoryName, createdAt, price, priceMin, priceMax, color, size
        );
        return ResponseEntity.ok(products);
    }
    @GetMapping("/best-sellers")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get best sellers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of best seller products",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class)))})
    })
    public List<ProductResponseDto> getBestSellers() {
        List<ProductResponseDto> bestSellers = service.getBestSellers();
        log.info("{}: Retrieved best sellers, page size: ", LogEnum.CONTROLLER);
        return bestSellers;
    }

    @GetMapping("/on-sale")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get products with discounted prices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products with discounted prices",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class)))}),
            @ApiResponse(responseCode = "404", description = "No products found with discounted prices",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))})
    })
    public List<ProductResponseDto> getOnSaleProducts() {
        List<ProductResponseDto> onSaleProducts = service.findByOldPriceGreaterThanCurrentPrice();
        log.info("{}: Retrieved on sale products", LogEnum.CONTROLLER);
        return onSaleProducts;
    }
    @PatchMapping("/{productId}/price")
    public ResponseUpdatePriceDto updateProductPrice(
            @PathVariable String productId,
            @Valid @RequestBody RequestUpdatePriceDto requestUpdatePriceDto
    ) {
        return service.updateProductPrice(productId, requestUpdatePriceDto);
    }
}
