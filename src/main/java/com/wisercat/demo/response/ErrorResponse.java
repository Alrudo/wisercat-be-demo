package com.wisercat.demo.response;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ErrorResponse {
    private String status;
    private String message;
    private Long timestamp;
}
