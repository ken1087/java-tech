package com.kang.spring_webflux.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Customer {

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}