package com.kang.spring_webflux.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.kang.spring_webflux.entity.Customer;

import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {

    @Query("SELECT * FROM customer WHERE last_name = :lastname")
    Flux<Customer> findByLastName(String lastName);

}