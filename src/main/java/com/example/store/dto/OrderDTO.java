package com.example.store.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderDTO implements Serializable {
    private Long orderId;
    private OrderCustomerDTO customer;
    private List<OrderProductDTO> products;
}
