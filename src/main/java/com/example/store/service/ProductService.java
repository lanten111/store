package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.exception.exceptions.NotFoundException;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductDTO createProduct(ProductDTO productDTO){
        Optional<Product> optionalProduct =  repository.findByName(productDTO.getName());
        if ( optionalProduct.isPresent()){
            return mapper.productToProductDTO(repository.save(mapper.productDTOToProduct(productDTO)));
        } else {
            String message = String.format("Product with name %s already exist", productDTO.getName());
            throw new NotFoundException(message, message);
        }
    }

    public List<ProductDTO> getProducts(){
        return mapper.productsTOProductDTOs(repository.findAll());
    }

    public ProductDTO getProduct(Long id){
        Optional<Product> optionalProduct = repository.findById(id);
        if (optionalProduct.isPresent()) {
            return mapper.productToProductDTO(optionalProduct.get());
        }else {
            String message = String.format("Product with id %s not found", id);
            throw new NotFoundException(message, message);
        }
    }
}
