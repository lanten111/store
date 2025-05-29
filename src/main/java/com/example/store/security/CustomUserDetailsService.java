package com.example.store.security;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.exception.exceptions.NotFoundException;
import com.example.store.repository.CustomerRepository;
import com.example.store.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        if ( customerOptional.isPresent() ){
            return new UserDetailsImpl( customerOptional.get().getCustomerId(), customerOptional.get().getEmail(), customerOptional.get().getPassword());
        } else {
            String message = String.format("User with email %s does not exist", email);
            throw new NotFoundException(message, message);
        }
    }

//    private Collection < ? extends GrantedAuthority> mapRolesToAuthorities(Collection <Role> roles) {
//        Collection < ? extends GrantedAuthority> mapRoles = roles.stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//        return mapRoles;

}