package com.wisercat.demo.config;

import com.wisercat.demo.exception.CriterionNotFoundException;
import com.wisercat.demo.exception.FilterNotFoundException;
import com.wisercat.demo.exception.InvalidCriterionException;
import com.wisercat.demo.exception.NoCriterionProvided;
import com.wisercat.demo.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler({FilterNotFoundException.class, CriterionNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception e) {
        var response = ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(Instant.now().toEpochMilli())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ResponseBody
    @ExceptionHandler({InvalidCriterionException.class, NoCriterionProvided.class})
    public ResponseEntity<ErrorResponse> handleInvalidDataException(Exception e) {
        var response = ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(Instant.now().toEpochMilli())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
