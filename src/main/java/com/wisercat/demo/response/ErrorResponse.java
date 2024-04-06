package com.wisercat.demo.response;

import lombok.Builder;


@Builder
public class ErrorResponse {
    private String status;
    private String message;
    private Long timestamp;
}
