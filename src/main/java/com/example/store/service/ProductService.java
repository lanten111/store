package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.exception.exceptions.AlreadyExistException;
import com.example.store.exception.exceptions.NotFoundException;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @CachePut(cacheNames = PRODUCT_DTO_CACHE_NAME, key = "#result.productId")
    @CacheEvict(cacheNames = PRODUCT_LIST_DTO_CACHE_NAME, allEntries = true)
    public ProductDTO createProduct(ProductDTO productDTO) {
        Optional<Product> optionalProduct = productRepository.findByName(productDTO.getName());
        if (optionalProduct.isEmpty()) {
            logger.info("created product with name {}", productDTO.getName());
            return productMapper.productToProductDTO(
                    productRepository.save(productMapper.productDtoToProduct(productDTO)));
        } else {
            String message = String.format("Product with name %s already exist", productDTO.getName());
            logger.debug("Product with name {} already exist", productDTO.getName());
            throw new AlreadyExistException(message, message);
        }
    }

    @Cacheable(cacheNames = PRODUCT_LIST_DTO_CACHE_NAME, keyGenerator = "customKeyGenerator")
    public List<ProductDTO> getProducts() {
        return productMapper.productsToProductDTOs(productRepository.findAll());
    }

    @Cacheable(value = PRODUCT_DTO_CACHE_NAME, key = "#productId")
    public ProductDTO getProduct(Long productId) {
        return productMapper.productToProductDTO(getProductEntity(productId));
    }

    public Product getProductEntity(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            logger.info("got product with id {}", productId);
            return optionalProduct.get();
        } else {
            String message = String.format("Product with id %s not found", productId);
            logger.warn("Product with id {} not found", productId);
            throw new NotFoundException(message, message);
        }
    }
}
