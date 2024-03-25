package com.wisercat.demo.controller;

import com.wisercat.demo.service.CustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/demo")
public class Controller {
    private final CustomService customService;

    @GetMapping
    public String helloWorld() {
        return customService.helloWorld();
    }
}
