    package com.wisercat.demo.config;

    import com.wisercat.demo.exception.*;
    import com.wisercat.demo.response.ErrorResponse;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.bind.annotation.ResponseBody;
    import org.springframework.web.bind.annotation.RestControllerAdvice;

    import java.time.Instant;

    @RestControllerAdvice
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
        @ExceptionHandler({InvalidFilterException.class, InvalidCriterionException.class, NoCriterionProvided.class})
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
