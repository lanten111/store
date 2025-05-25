package com.example.store.service;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.exception.exceptions.AlreadyExistException;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public List<CustomerDTO> getAllCustomer(){
        return customerMapper.customersToCustomerDTOs(customerRepository.findAll());
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO){
        Optional<Customer> customerOptional = customerRepository.findByName(customerDTO.getName());
        if ( customerOptional.isEmpty() ){
            return customerMapper.customerToCustomerDTO(customerRepository.save(customerMapper.customerDtoToCustomer(customerDTO)));
        } else {
            String message = String.format("Customer with name %s already exist", customerDTO.getName());
            throw new AlreadyExistException(message, message);
        }
    }
}
