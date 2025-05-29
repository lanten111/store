package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.exception.exceptions.AlreadyExistException;
import com.example.store.exception.exceptions.NotFoundException;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductMapper productMapper;

    @InjectMocks
    ProductService productService;

    private final Long productId = 1L;

    @BeforeEach
    void setUp() {}

    @Test
    void CanSuccessfullyGetProductById() {
//        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(getProduct()));
        when(productService.getProductEntity(productId)).thenReturn(getProduct());
        when(productMapper.productToProductDTO(any())).thenReturn(getProductDTO());

        ProductDTO productDTO = productService.getProduct(productId);
        assertNotNull(productDTO);
    }

    @Test
    void CanThrowNotFoundExceptionWhenUsingInvalidId() {

        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            productService.getProductEntity(2L);
        });
    }

    @Test
    void CanSuccessfullyGetAllProducts() {
        when(productRepository.findAll()).thenReturn(getProducts());
        when(productMapper.productsTOProductDTOs(any())).thenReturn(getProductDTOs());

        List<ProductDTO> productDTOS = productService.getProducts();
        assertEquals(2, productDTOS.size());
        assertEquals(getProducts().get(0).getName(), productDTOS.get(0).getName());
        assertEquals(getProducts().get(1).getName(), productDTOS.get(1).getName());
        assertFalse(productDTOS.isEmpty());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void CanSuccessfullyCreateProduct() {
        when(productRepository.save(any())).thenReturn(getProduct());
        when(productRepository.findByName(any())).thenReturn(Optional.empty());
        when(productMapper.productToProductDTO(any())).thenReturn(getProductDTO());

        ProductDTO productDTO = productService.createProduct(getProductDTO());
        assertNotNull(productDTO);
        assertEquals(productDTO.getName(), getProductDTO().getName());
        verify(productRepository, times(1)).findByName(any(String.class));
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void CanThrowAlreadyExistWhenCreatingProductWithNameThatExist() {

        when(productRepository.findByName(getProductDTO().getName())).thenReturn(Optional.of(getProduct()));
        assertThrows(AlreadyExistException.class, () -> {
            productService.createProduct(getProductDTO());
        });
    }

    public List<Product> getProducts() {

        List<Product> products = new LinkedList<>();
        Product product = new Product();
        product.setName("product1");
        product.setDescription("product1 description");
        product.setProductId(productId);

        products.add(product);
        product = new Product();
        product = new Product();
        product.setName("product2");
        product.setDescription("product2 description");
        product.setProductId(productId);
        products.add(product);
        return products;
    }

    public List<ProductDTO> getProductDTOs() {

        List<ProductDTO> productDTOS = new LinkedList<>();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("product1");
        productDTO.setDescription("product1 description");
        productDTO.setProductId(productId);

        productDTOS.add(productDTO);
        productDTO = new ProductDTO();
        productDTO.setName("product2");
        productDTO.setDescription("product2 description");
        productDTO.setProductId(productId);
        productDTOS.add(productDTO);
        return productDTOS;
    }

    public Product getProduct() {
        Product product = new Product();
        product.setName("product");
        product.setDescription("product description");
        product.setProductId(productId);
        return product;
    }

    public ProductDTO getProductDTO() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("product");
        productDTO.setDescription("product description");
        productDTO.setProductId(productId);
        return productDTO;
    }
}
