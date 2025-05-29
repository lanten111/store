package com.example.store.service;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.exception.exceptions.AlreadyExistException;
import com.example.store.exception.exceptions.NotFoundException;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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

    public void login(CustomerDTO customerDTO, HttpServletRequest request){
        authenticationService.login(customerDTO, request);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        authenticationService.logout(request, response, authentication);
    }

    @Cacheable(value = CUSTOMER_LIST_DTO_CACHE_NAME, keyGenerator = "customKeyGenerator")
    public List<CustomerDTO> getAllCustomer(){
        return customerMapper.customersToCustomerDTOs(customerRepository.findAll());
    }

    @CacheEvict(cacheNames = {CUSTOMER_LIST_DTO_CACHE_NAME,CUSTOMER_SEARCH_LIST_DTO_CACHE_NAME}, allEntries = true)
    @CachePut(cacheNames = CUSTOMER_LIST_DTO_CACHE_NAME, key = "#result.customerId")
    public CustomerDTO createCustomer(CustomerDTO customerDTO){
        Optional<Customer> customerOptional = customerRepository.findByName(customerDTO.getName());
        if ( customerOptional.isEmpty() ){
            customerDTO.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
            return customerMapper.customerToCustomerDTO(customerRepository.save(customerMapper.customerDtoToCustomer(customerDTO)));
        } else {
            String message = String.format("Customer with name %s already exist", customerDTO.getName());
            throw new AlreadyExistException(message, message);
        }
    }

    @Cacheable(value = CUSTOMER_SEARCH_LIST_DTO_CACHE_NAME, key = "#query")
    public List<CustomerDTO> customerSearch(String query){
        List<Customer> customerList = customerRepository.findByNameContainsIgnoreCase(query);
        if ( customerList.isEmpty() ){
            String message = String.format("No customer containing query word %s exist", query);
            throw new NotFoundException(message, message);
        } else {
            return customerMapper.customersToCustomerDTOs(customerList);
        }
    }


}
