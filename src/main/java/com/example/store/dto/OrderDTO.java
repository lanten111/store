package com.example.store.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDTO implements Serializable {
    private Long id;
    private String description;
    private OrderCustomerDTO customer;
}
