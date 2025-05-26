package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.exception.exceptions.AlreadyExistException;
import com.example.store.exception.exceptions.NotFoundException;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.store.config.cache.CacheNames.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @CachePut(cacheNames = PRODUCT_DTO_CACHE_NAME, key = "#result.productId")
    @CacheEvict(cacheNames = PRODUCT_LIST_DTO_CACHE_NAME, allEntries = true)
    public ProductDTO createProduct(ProductDTO productDTO){
        Optional<Product> optionalProduct =  productRepository.findByName(productDTO.getName());
        if ( optionalProduct.isEmpty()){
            return productMapper.productToProductDTO(productRepository.save(productMapper.productDTOToProduct(productDTO)));
        } else {
            String message = String.format("Product with name %s already exist", productDTO.getName());
            throw new AlreadyExistException(message, message);
        }
    }

    @Cacheable(cacheNames = PRODUCT_LIST_DTO_CACHE_NAME, keyGenerator = "customKeyGenerator")
    public List<ProductDTO> getProducts(){
        return productMapper.productsTOProductDTOs(productRepository.findAll());
    }

    @Cacheable(value = ORDER_DTO_CACHE_NAME, key = "#productId")
    public ProductDTO getProduct(Long productId){
            return productMapper.productToProductDTO(getProductEntity(productId));
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
