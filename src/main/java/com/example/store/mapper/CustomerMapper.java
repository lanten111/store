package com.example.store.mapper;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "password", ignore = true)
    CustomerDTO customerToCustomerDTO(Customer customer);

    @Mapping(target = "password", ignore = true)
    List<CustomerDTO> customersToCustomerDTOs(List<Customer> customer);

    Customer customerDtoToCustomer(CustomerDTO customerDTO);
}
