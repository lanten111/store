package com.example.store.dto;

import com.example.store.validation.OnCreate;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderDTO implements Serializable {

    private Long orderId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @NotNull(message = "{order.create.empty.customer}", groups = OnCreate.class)
    private OrderCustomerDTO customer;
    @NotEmpty(message = "{order.create.empty.product}", groups = OnCreate.class)
    private List<OrderProductDTO> products;
}
