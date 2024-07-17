package com.example.demo.repositories;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.CustomerModel;

public interface CustomerRepository extends JpaRepository<CustomerModel, Long> {
    Page<CustomerModel> findAllByIsActiveTrue(Pageable pageable);

    Page<CustomerModel> findByCustomerNameIgnoreCase(String itemName, Pageable pageable);
    
}
