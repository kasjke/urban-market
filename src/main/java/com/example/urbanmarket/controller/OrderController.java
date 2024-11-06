package com.example.urbanmarket.controller;

import com.example.urbanmarket.dto.request.OrderRequestDto;
import com.example.urbanmarket.dto.response.OrderResponseDto;
import com.example.urbanmarket.entity.order.OrderService;
import com.example.urbanmarket.exception.LogEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
@Slf4j
public class OrderController {

    private static final String URI_WITH_ID = "/{id}";
    private final OrderService orderService;
    private static final String OBJECT_NAME = "Order";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new order", description = "Creates a new order with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data provided")
    })
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto createdOrder = orderService.create(orderRequestDto);
        log.info("{}: {} created: {}", LogEnum.CONTROLLER, OBJECT_NAME, createdOrder);
        return createdOrder;
    }

    @GetMapping(URI_WITH_ID)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public OrderResponseDto getOrderById(@PathVariable String id) {
        OrderResponseDto order = orderService.getById(id);
        log.info("{}: Get {} by id: {}", LogEnum.CONTROLLER, OBJECT_NAME, id);
        return order;
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get orders by user ID", description = "Retrieve all orders made by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No orders found for the user")
    })
    public List<OrderResponseDto> getOrdersByUserId(@PathVariable String userId) {
        List<OrderResponseDto> orders = orderService.getAllByUserId(userId);
        log.info("{}: Get all orders for user with id: {}", LogEnum.CONTROLLER, userId);
        return orders;
    }

    @PutMapping(URI_WITH_ID)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an order", description = "Updates the details of an existing order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data provided"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public OrderResponseDto updateOrder(@PathVariable String id, @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto updatedOrder = orderService.update(id, orderRequestDto);
        log.info("{}: Updated {} with id: {}", LogEnum.CONTROLLER, OBJECT_NAME, id);
        return updatedOrder;
    }

    @DeleteMapping(URI_WITH_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an order", description = "Deletes an order by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public void deleteOrder(@PathVariable String id) {
        orderService.delete(id);
        log.info("{}: Deleted {} with id: {}", LogEnum.CONTROLLER, OBJECT_NAME, id);
    }
}
