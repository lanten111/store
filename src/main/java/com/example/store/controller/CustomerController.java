package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.service.CustomerService;
import com.example.store.validation.OnCreate;
import com.example.store.validation.OnLogin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1//customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Validated(OnLogin.class) @RequestBody CustomerDTO customerDTO, HttpServletRequest request) {
        customerService.login(customerDTO, request);
        return new ResponseEntity<>("User Logged in successfully", HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        customerService.logout(request, response, authentication);
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomerDTO> createCustomer(@Validated(OnCreate.class) @RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity<>(customerService.createCustomer(customerDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomer(), HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<List<CustomerDTO>> searchCustomer(@PathVariable @NotBlank String query) {
        return new ResponseEntity<>(customerService.customerSearch(query), HttpStatus.CREATED);
    }
}
