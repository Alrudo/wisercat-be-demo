package com.wisercat.demo.config;

import com.wisercat.demo.exception.FilterNotFoundException;
import com.wisercat.demo.response.NotFoundResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(FilterNotFoundException.class)
    public ResponseEntity<NotFoundResponse> handleFilterNotFoundException(Exception e) {
        var response = NotFoundResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(Instant.now().toEpochMilli())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
