package com.example.store.service;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.exception.exceptions.NotFoundException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderDTO getOrderById(Long orderId){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if ( optionalOrder.isPresent() ){
            return orderMapper.orderToOrderDTO(optionalOrder.get());
        } else {
            String message = String.format("Order with id %s not found", orderId);
            throw new NotFoundException(message, message);
        }
    }

    public OrderDTO createOrder(OrderDTO orderDTO){
        return orderMapper.orderToOrderDTO(orderRepository.save(orderMapper.orderDtoToOrder(orderDTO)));
    }

    public List<OrderDTO> getAllOrders(){
        return orderMapper.ordersToOrderDTOs(orderRepository.findAll());
    }
}
