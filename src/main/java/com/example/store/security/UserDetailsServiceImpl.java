package com.example.store.security;

import com.example.store.entity.Customer;
import com.example.store.repository.CustomerRepository;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private CustomerRepository customerRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        if (customerOptional.isPresent()) {
            return new UserDetailsImpl(
                    customerOptional.get().getCustomerId(),
                    customerOptional.get().getEmail(),
                    customerOptional.get().getPassword());
        } else {
            String message = String.format("User with email %s does not exist", email);
            throw new BadCredentialsException(message);
        }
    }
}
