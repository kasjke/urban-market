package com.example.urbanmarket.entity.order;

import com.example.urbanmarket.dto.request.OrderRequestDto;
import com.example.urbanmarket.dto.response.OrderResponseDto;
import com.example.urbanmarket.exception.exceptions.CustomNotFoundException;
import com.example.urbanmarket.exception.LogEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final String OBJECT_NAME = "Order";

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto create(OrderRequestDto orderRequestDto) {
        OrderEntity orderEntity = orderMapper.toEntity(orderRequestDto);
        orderEntity.setCreatedAt(LocalDateTime.now());
        OrderEntity savedOrder = orderRepository.save(orderEntity);

        log.info("{}: {} (Id: {}) was created", LogEnum.SERVICE, OBJECT_NAME, savedOrder.getId());
        return orderMapper.toResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto getById(String id) {
        OrderEntity orderEntity = findById(id);

        log.info("{}: {} (Id: {}) was found", LogEnum.SERVICE, OBJECT_NAME, id);
        return orderMapper.toResponseDto(orderEntity);
    }

    @Override
    public List<OrderResponseDto> getAllByUserId(String userId) {
        log.info("{}: Fetching {}s for userId: {}", LogEnum.SERVICE, OBJECT_NAME, userId);
        return orderMapper.toResponseDtoList(orderRepository.findByUserId(userId));
    }

    @Override
    public OrderResponseDto update(String id, OrderRequestDto orderRequestDto) {
        OrderEntity orderEntity = findById(id);

        orderMapper.updateOrderFromDto(orderRequestDto, orderEntity);
        orderEntity.setUpdatedAt(LocalDateTime.now());

        OrderEntity updatedOrderEntity = orderRepository.save(orderEntity);
        log.info("{}: {} (Id: {}) was updated", LogEnum.SERVICE, OBJECT_NAME, id);
        return orderMapper.toResponseDto(updatedOrderEntity);
    }

    @Override
    public void delete(String id) {
        orderRepository.deleteById(id);
        log.info("{}: {} (Id: {}) was deleted", LogEnum.SERVICE, OBJECT_NAME, id);
    }

    public OrderEntity findById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("{}: {} (Id: {}) not found", LogEnum.SERVICE, OBJECT_NAME, id);
                    return new CustomNotFoundException(OBJECT_NAME, id);
                });
    }
}
