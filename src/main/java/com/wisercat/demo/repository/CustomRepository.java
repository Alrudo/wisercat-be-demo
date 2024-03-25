package com.wisercat.demo.repository;

import org.springframework.stereotype.Repository;

@Repository
public class CustomRepository {

    public String helloWorld() {
        return "Hello World";
    }
}
