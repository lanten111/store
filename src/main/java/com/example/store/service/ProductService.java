package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.exception.exceptions.AlreadyExistException;
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

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductDTO createProduct(ProductDTO productDTO){
        Optional<Product> optionalProduct =  productRepository.findByName(productDTO.getName());
        if ( optionalProduct.isEmpty()){
            return productMapper.productToProductDTO(productRepository.save(productMapper.productDTOToProduct(productDTO)));
        } else {
            String message = String.format("Product with name %s already exist", productDTO.getName());
            throw new AlreadyExistException(message, message);
        }
    }

    public List<ProductDTO> getProducts(){
        return productMapper.productsTOProductDTOs(productRepository.findAll());
    }

    public ProductDTO getProduct(Long id){
            return productMapper.productToProductDTO(getProductEntity(id));
    }

    protected Product getProductEntity(Long id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }else {
            String message = String.format("Product with id %s not found", id);
            throw new NotFoundException(message, message);
        }
    }
}
