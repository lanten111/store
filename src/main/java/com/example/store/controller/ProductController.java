package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.service.ProductService;
import com.example.store.validation.OnCreate;
import com.example.store.validation.OnGet;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Validated(OnCreate.class) @RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts() {
        return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@Validated(OnGet.class) @PathVariable long productId) {
        return new ResponseEntity<>(productService.getProduct(productId), HttpStatus.OK);
    }
}
