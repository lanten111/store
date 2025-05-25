package com.example.store.repository;

import com.example.store.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByName(String name);

    @Query(value = "SELECT * FROM customer WHERE LOWER(name) LIKE %:query%", nativeQuery = true)
    List<Customer> findByNameSubstring(@Param("query") String query);

    List<Customer> findByNameContainsIgnoreCase(String query);
}
