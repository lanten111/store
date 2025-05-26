package com.example.store.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductOrderDTO  implements Serializable {
    private Long orderId;
}
