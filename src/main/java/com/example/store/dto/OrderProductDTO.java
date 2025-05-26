package com.example.store.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderProductDTO implements Serializable {
    private Long productId;
    private String name;
}
