package com.example.store.service;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.exception.exceptions.AlreadyExistException;
import com.example.store.exception.exceptions.NotFoundException;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//TODO can add more test to test exception and validation
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CustomerMapper customerMapper;

    @InjectMocks
    CustomerService customerService;

    private final Long customerId = 1L;

    @BeforeEach
    void setUp() {}

    @Test
    void CanThrowNotFoundExceptionWhenUsingInvalidId() {

        when(customerRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            customerService.getCustomerById(2L);
        });
    }

    @Test
    void CanSuccessfullyGetAllProducts() {
        when(customerRepository.findAll()).thenReturn(getCustomers());
        when(customerMapper.customersToCustomerDTOs(any())).thenReturn(getCustomerDTOs());

        List<CustomerDTO> customerDTOS = customerService.getAllCustomer();
        assertEquals(2, customerDTOS.size());
        assertFalse(customerDTOS.isEmpty());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void CanSuccessfullyCreateProduct() {
        when(customerRepository.save(any())).thenReturn(getCustomer());
        when(customerRepository.findByName(any())).thenReturn(Optional.empty());
        when(customerMapper.customerToCustomerDTO(any())).thenReturn(getCustomerDto());

        CustomerDTO customerDTO = customerService.createCustomer(getCustomerDto());
        assertNotNull(customerDTO);
        assertEquals(customerDTO.getName(), getCustomerDto().getName());
        verify(customerRepository, times(1)).findByName(any(String.class));
        verify(customerRepository, times(1)).save(any());
    }

    @Test
    void CanThrowAlreadyExistWhenCreatingProductWithNameThatExist() {

        when(customerRepository.findByName(getCustomerDto().getName())).thenReturn(Optional.of(getCustomer()));
        assertThrows(AlreadyExistException.class, () -> {
            customerService.createCustomer(any());
        });
    }

    public List<Customer> getCustomers() {

        List<Customer> customers = new LinkedList<>();
        Customer customer = new Customer();
        customer.setName("user1");
        customer.setEmail("user1@email");
        customer.setPassword("P@assword");

        customers.add(customer);
        customer = new Customer();
        customer.setName("user2");
        customer.setEmail("user2@email");
        customer.setPassword("P@assword");
        customers.add(customer);

        return customers;
    }

    public List<CustomerDTO> getCustomerDTOs() {

        List<CustomerDTO> customerDTOS = new LinkedList<>();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("user1");
        customerDTO.setEmail("user1@email");
        customerDTO.setPassword("P@assword");

        customerDTOS.add(customerDTO);
        customerDTO = new CustomerDTO();
        customerDTO.setName("user1");
        customerDTO.setEmail("user1@email");
        customerDTO.setPassword("P@assword");
        customerDTOS.add(customerDTO);
        return customerDTOS;
    }

    public Customer getCustomer() {
        Customer customer = new Customer();
        customer.setName("user1");
        customer.setEmail("user1@email");
        customer.setPassword("P@assword");
        return customer;
    }

    public CustomerDTO getCustomerDto() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("user1");
        customerDTO.setEmail("user1@email");
        customerDTO.setPassword("P@assword");
        return customerDTO;
    }
}
