package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO){
        return productService.createProduct(productDTO);
    }

    @GetMapping
    public List<ProductDTO> getProducts(){
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable long id){
        return productService.getProduct(id);
    }
}
