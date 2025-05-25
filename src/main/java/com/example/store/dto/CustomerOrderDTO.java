package com.example.store.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerOrderDTO implements Serializable {
    private Long id;
    private String description;
}
