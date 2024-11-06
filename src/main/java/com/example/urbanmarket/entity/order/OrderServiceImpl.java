package com.example.urbanmarket.entity.order;

import com.example.urbanmarket.dto.request.OrderRequestDto;
import com.example.urbanmarket.dto.response.OrderResponseDto;
import com.example.urbanmarket.exception.CustomNotFoundException;
import com.example.urbanmarket.exception.LogEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private static final String OBJECT_NAME = "Order";

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
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));

        log.info("{}: {} (Id: {}) was found", LogEnum.SERVICE, OBJECT_NAME, id);
        return orderMapper.toResponseDto(orderEntity);
    }

    @Override
    public List<OrderResponseDto> getAllByUserId(String userId) {
        log.info("{}: Fetching {}s for userId: {}", LogEnum.SERVICE, OBJECT_NAME, userId);
        return orderRepository.findByUserId(userId)
                .stream()
                .map(orderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto update(String id, OrderRequestDto orderRequestDto) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));

        orderMapper.updateOrderFromDto(orderRequestDto, orderEntity);
        orderEntity.setUpdatedAt(LocalDateTime.now());

        OrderEntity updatedOrderEntity = orderRepository.save(orderEntity);
        log.info("{}: {} (Id: {}) was updated", LogEnum.SERVICE, OBJECT_NAME, id);
        return orderMapper.toResponseDto(updatedOrderEntity);
    }

    @Override
    public void delete(String id) {
        if (!orderRepository.existsById(id)) {
            throw new CustomNotFoundException(OBJECT_NAME, id);
        }
        orderRepository.deleteById(id);
        log.info("{}: {} (Id: {}) was deleted", LogEnum.SERVICE, OBJECT_NAME, id);
    }
}
