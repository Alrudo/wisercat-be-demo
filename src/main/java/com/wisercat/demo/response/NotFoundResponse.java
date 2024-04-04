package com.wisercat.demo.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotFoundResponse {
    private Integer status;
    private String message;
    private Long timestamp;
}
