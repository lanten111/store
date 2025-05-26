package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.service.ProductService;
import com.example.store.validation.OnCreate;
import com.example.store.validation.OnGet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct( @Validated(OnCreate.class) @RequestBody ProductDTO productDTO ){
        return productService.createProduct(productDTO);
    }

    @GetMapping
    public List<ProductDTO> getProducts(){
        return productService.getProducts();
    }

    @GetMapping("/{productId}")
    public ProductDTO getProduct(@Validated( OnGet.class)  @PathVariable long productId ){
        return productService.getProduct(productId);
    }
}
