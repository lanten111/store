package com.example.store.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CustomerDTO implements Serializable {
    private Long id;
    private String name;
    private List<CustomerOrderDTO> orders;
}
