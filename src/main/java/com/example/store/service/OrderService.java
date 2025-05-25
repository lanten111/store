package com.example.store.service;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.exception.exceptions.NotFoundException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.store.config.cache.CacheNames.ORDER_DTO_CACHE_NAME;
import static com.example.store.config.cache.CacheNames.ORDER_LIST_DTO_CACHE_NAME;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Cacheable(value = ORDER_DTO_CACHE_NAME, key = "#orderId")
    public OrderDTO getOrderById(Long orderId){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if ( optionalOrder.isPresent() ){
            return orderMapper.orderToOrderDTO(optionalOrder.get());
        } else {
            String message = String.format("Order with id %s not found", orderId);
            throw new NotFoundException(message, message);
        }
    }

    @CachePut(cacheNames = ORDER_DTO_CACHE_NAME, key = "#result.id")
    @CacheEvict(cacheNames = ORDER_LIST_DTO_CACHE_NAME, allEntries = true)
    public OrderDTO createOrder(OrderDTO orderDTO){
        return orderMapper.orderToOrderDTO(orderRepository.save(orderMapper.orderDtoToOrder(orderDTO)));
    }


    @Cacheable(cacheNames = ORDER_LIST_DTO_CACHE_NAME, keyGenerator = "customKeyGenerator")
    public List<OrderDTO> getAllOrders(){
        return orderMapper.ordersToOrderDTOs(orderRepository.findAll());
    }
}
