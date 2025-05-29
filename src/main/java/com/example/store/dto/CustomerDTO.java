package com.example.store.dto;

import com.example.store.validation.OnCreate;
import com.example.store.validation.OnLogin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CustomerDTO implements Serializable {
    private Long customerId;
    @NotBlank(message = "{customer.create.empty.name}", groups = OnCreate.class)
    @Size(min = 8, max = 20 , message = "{product.create.min.max.name}")
    private String name;
    @NotBlank(message = "{customer.create.empty.email}", groups = {OnCreate.class, OnLogin.class})
    private String email;
    @NotBlank(message = "{customer.create.empty.password}", groups = {OnCreate.class, OnLogin.class})
    private String password;
    private List<CustomerOrderDTO> orders;
}
