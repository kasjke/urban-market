package com.example.urbanmarket.entity.order;


import com.example.urbanmarket.dto.request.OrderRequestDto;
import com.example.urbanmarket.dto.response.OrderResponseDto;
import com.example.urbanmarket.exception.orderExceptions.OrderNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper ;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        OrderEntity orderEntity = orderMapper.toEntity(orderRequestDto);
        orderEntity.setCreatedAt(LocalDateTime.now());
        OrderEntity savedOrder = orderRepository.save(orderEntity);
        return orderMapper.toResponseDto(savedOrder);
    }

    @Override
    public Optional<OrderResponseDto> getOrderById(String id) {
        return orderRepository.findById(id)
                .map(orderMapper::toResponseDto);
    }

    @Override
    public List<OrderResponseDto> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(orderMapper::toResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public OrderResponseDto updateOrder(String id, OrderRequestDto orderRequestDto) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        orderMapper.updateOrderFromDto(orderRequestDto, orderEntity);
        orderEntity.setUpdatedAt(LocalDateTime.now());

        OrderEntity updatedOrderEntity = orderRepository.save(orderEntity);
        return orderMapper.toResponseDto(updatedOrderEntity);
    }

    @Override
    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}
