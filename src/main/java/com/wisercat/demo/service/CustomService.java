package com.wisercat.demo.service;

import com.wisercat.demo.repository.CustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomService {
    private final CustomRepository customRepository;

    public String helloWorld() {
        return customRepository.helloWorld();
    }
}
