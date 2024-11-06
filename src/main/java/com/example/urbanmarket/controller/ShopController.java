package com.example.urbanmarket.controller;

import com.example.urbanmarket.dto.request.ShopRequestDto;
import com.example.urbanmarket.dto.response.ShopResponseDto;
import com.example.urbanmarket.entity.shop.ShopService;
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

@RestController
@RequestMapping("/api/v1/shops")
@Slf4j
@RequiredArgsConstructor
public class ShopController {
    private static final String URI_WITH_ID = "/{id}";
    //private static final String SEC_REC = "BearerAuth";
    private static final String OBJECT_NAME = "Shop";

    private final ShopService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //@SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Add new Shop")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new Shop",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ShopResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = RuntimeException.class))})
    })
    public ShopResponseDto create(@Valid @RequestBody ShopRequestDto request) {
        //ac    cessValidator.isAdmin();

        ShopResponseDto shop = service.create(request);

        log.info("{}: {} (id: {}) has been added", LogEnum.SERVICE, OBJECT_NAME, shop.id());
        return shop;
    }

    @GetMapping
    @Operation(summary = "Get all shops")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of shops",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ShopResponseDto.class)))}
            )
    })
    public List<ShopResponseDto> getAll() {
        List<ShopResponseDto> all = service.getAll();

        log.info("{}: {}s have been retrieved", LogEnum.CONTROLLER, OBJECT_NAME);
        return all;
    }

    @GetMapping(URI_WITH_ID)
    //@SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Get shop by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shop received",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ShopResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Shop not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class)) })

    })
    public ShopResponseDto getById(@PathVariable String id) {
        ShopResponseDto shop = service.getById(id);
        log.info("{}: {} (id: {}) has been retrieved", LogEnum.SERVICE, OBJECT_NAME, shop.id());
        return shop;
    }

    @PutMapping(URI_WITH_ID)
    //@SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Update shop")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shop updated"),
            @ApiResponse(responseCode = "404", description = "Shop not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))})
    })
    public ShopResponseDto update(@PathVariable String id, @Valid @RequestBody ShopRequestDto requestDto) {
        //accessValidator.isAdmin();

        ShopResponseDto shop = service.update(id, requestDto);
        log.info("{}: {} (id: {}) has been updated", LogEnum.CONTROLLER, OBJECT_NAME, id);
        return shop;
    }

    @DeleteMapping(URI_WITH_ID)
    //@SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Delete shop")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shop deleted"),
            @ApiResponse(responseCode = "404", description = "Shop not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class)) }) })
    public void delete(@PathVariable String id){
        //accessValidator.isAdmin();

        service.delete(id);
        log.info("{}: {} (id: {}) has been deleted", LogEnum.CONTROLLER, OBJECT_NAME, id);
    }
}
