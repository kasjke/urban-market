package com.example.urbanmarket.controller;

import com.example.urbanmarket.dto.request.CartRequestDto;
import com.example.urbanmarket.dto.response.CartResponseDto;
import com.example.urbanmarket.entity.cart.CartService;
import com.example.urbanmarket.exception.LogEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@Slf4j
@RequiredArgsConstructor
public class CartController {
    private static final String URI_WITH_ID = "/{id}";
    //private static final String SEC_REC = "BearerAuth";
    private static final String OBJECT_NAME = "Cart";

    private final CartService cartService;
    //private final AccessValidator accessValidator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //@SecurityRequirement(name = SEC_REC)
    @Operation(description = "create a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the cart",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CartResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))}
            ),
    })
    public CartResponseDto create(@RequestBody CartRequestDto cartDto) throws RuntimeException {
        CartResponseDto cart = cartService.create(cartDto);
        log.info("{}: {} (id: {}) has been added", LogEnum.CONTROLLER, OBJECT_NAME, cart.id());
        return cart;
    }

    @GetMapping()
    @Operation(summary = "Get all carts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of carts",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CartResponseDto.class)))}
            )
    })
    public List<CartResponseDto> getAll() throws RuntimeException {
        //accessValidator.isAdmin();

        List<CartResponseDto> cartList = cartService.getAll();
        log.info("{}: {} list has been retrieved", LogEnum.CONTROLLER, OBJECT_NAME);
        return cartList;
    }

    @GetMapping(URI_WITH_ID)
    @Operation(description = "get a cart by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CartResponseDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))
                    })
    })
    public CartResponseDto getById(@PathVariable String id) throws RuntimeException {
        //accessValidator.hasPermission(OBJECT_NAME, id);

        CartResponseDto cart = cartService.getById(id);
        log.info("{}: {} (id: {}) has been retrieved", LogEnum.CONTROLLER, OBJECT_NAME, id);
        return cart;
    }

    @PutMapping(URI_WITH_ID)
    //@SecurityRequirement(name = SEC_REC)
    @Operation(description = "update a cart by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the cart",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CartResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))
                    })
    })
    public CartResponseDto update(@PathVariable String id, @RequestBody CartRequestDto cartDto) throws RuntimeException {
        //accessValidator.hasPermission(OBJECT_NAME, id);

        CartResponseDto cart = cartService.update(id, cartDto);
        log.info("{}: {} (id: {}) has been updated", LogEnum.CONTROLLER, OBJECT_NAME, cart.id());
        return cart;
    }

    @DeleteMapping(URI_WITH_ID)
    //@SecurityRequirement(name = SEC_REC)
    @Operation(description = "delete a cart by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the cart",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CartResponseDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RuntimeException.class))
                    })
    })
    public void delete(@PathVariable String id) throws RuntimeException {
        //accessValidator.isAdmin();

        cartService.delete(id);
        log.info("{}: {} (id: {}) has been deleted", LogEnum.CONTROLLER, OBJECT_NAME, id);
    }
}