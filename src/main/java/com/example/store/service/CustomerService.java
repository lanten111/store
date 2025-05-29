package com.example.store.service;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.TokenDTO;
import com.example.store.entity.Customer;
import com.example.store.exception.exceptions.AlreadyExistException;
import com.example.store.exception.exceptions.NotFoundException;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.security.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.store.config.cache.CacheNames.CUSTOMER_LIST_DTO_CACHE_NAME;
import static com.example.store.config.cache.CacheNames.CUSTOMER_SEARCH_LIST_DTO_CACHE_NAME;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    public TokenDTO login(CustomerDTO customerDTO, HttpServletRequest request) {
        return authenticationService.login(customerDTO, request);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        authenticationService.logout(request, response, authentication);
    }

    @Cacheable(value = CUSTOMER_LIST_DTO_CACHE_NAME, keyGenerator = "customKeyGenerator")
    public List<CustomerDTO> getAllCustomer() {
        return customerMapper.customersToCustomerDTOs(customerRepository.findAll());
    }

    @CacheEvict(
            cacheNames = {CUSTOMER_LIST_DTO_CACHE_NAME, CUSTOMER_SEARCH_LIST_DTO_CACHE_NAME},
            allEntries = true)
    @CachePut(cacheNames = CUSTOMER_LIST_DTO_CACHE_NAME, key = "#result.customerId")
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(customerDTO.getEmail());
        if (customerOptional.isEmpty()) {
            customerDTO.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
            Customer customer = customerRepository.save(customerMapper.customerDtoToCustomer(customerDTO));
            customer.setPassword(null);
            return customerMapper.customerToCustomerDTO(customer);
        } else {
            logger.warn("Customer with email {} already exist", customerDTO.getEmail());
            String message = String.format("Customer with email %s already exist", customerDTO.getEmail());
            throw new AlreadyExistException(message, message);
        }
    }

    @Cacheable(value = CUSTOMER_SEARCH_LIST_DTO_CACHE_NAME, key = "#query")
    public List<CustomerDTO> customerSearch(String query) {
        List<Customer> customerList = customerRepository.findByNameContainsIgnoreCase(query);
        if (customerList.isEmpty()) {
            String message = String.format("No customer containing query word %s exist", query);
            throw new NotFoundException(message, message);
        } else {
            return customerMapper.customersToCustomerDTOs(customerList);
        }
    }
}
