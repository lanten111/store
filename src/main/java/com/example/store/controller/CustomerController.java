package com.example.store.controller;

import com.example.store.dto.CustomerDTO;

import com.example.store.service.CustomerService;
import com.example.store.validation.OnCreate;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomer();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(@Validated(OnCreate.class)  @RequestBody CustomerDTO customerDTO) {
        return customerService.createCustomer(customerDTO);
    }

    @GetMapping("/search/{query}")
    public List<CustomerDTO> searchCustomer( @PathVariable @NotBlank  String query ){
        return customerService.customerSearch(query);
    }
}
