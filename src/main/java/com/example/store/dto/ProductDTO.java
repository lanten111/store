package com.example.store.dto;

import com.example.store.validation.OnCreate;
import com.example.store.validation.OnGet;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductDTO implements Serializable {

    @NotNull(message = "{product.get.empty.query}", groups = OnGet.class) private Long productId;

    @NotBlank(message = "{product.create.empty.name}", groups = OnCreate.class)
    @Size(min = 8, max = 20, message = "{product.create.min.max.name}", groups = OnCreate.class)
    private String name;

    @Size(min = 8, max = 20, message = "{product.create.min.max.description}", groups = OnCreate.class)
    @NotBlank(message = "Product description cannot be empty", groups = OnCreate.class)
    private String description;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ProductOrderDTO> orders;
}
