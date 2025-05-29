package com.example.store.dto;

import com.example.store.validation.OnCreate;
import com.example.store.validation.OnLogin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Email(message = "{customer.create.invalid.email}", groups = {OnCreate.class, OnLogin.class})
    private String email;
    @NotBlank(message = "{customer.create.empty.password}", groups = {OnCreate.class, OnLogin.class})
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=-]).{8,}$", message = "{customer.create.invalid.password}", groups = {OnCreate.class, OnLogin.class})
    private String password;
    private List<CustomerOrderDTO> orders;
}
