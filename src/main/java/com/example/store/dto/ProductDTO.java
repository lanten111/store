package com.example.store.dto;

import com.example.store.entity.Order;
import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {

    private Long productId;
    private String name;
    private String description;
    private List<ProductOrderDTO> orders;
}
