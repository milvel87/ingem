package com.ingem.interview.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private final CustomRequestErrorResponse customRequestErrorResponse;

    public CustomExceptionHandler(CustomRequestErrorResponse customRequestErrorResponse) {
        this.customRequestErrorResponse = customRequestErrorResponse;
    }


    @ExceptionHandler(CustomRequestErrorException.class)
    public ResponseEntity<CustomRequestErrorResponse> handleCustomRequestErrorException(CustomRequestErrorException ex) {

        Map<String, List<String>> errors = new HashMap<>();
        errors.put(ex.getAttribute(), Arrays.asList(ex.getReason()));
        return prepareExceptionResponse(ex, ex.getStatus(), errors);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        Map<String, List<String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getField(), e.getDefaultMessage()))
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        body.put("errors", errors);

        if (errors.size() == 0) {
            body.put("message", HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else {
            body.put("message", "");
        }
        return new ResponseEntity<>(body, headers, status);
    }

    private ResponseEntity<CustomRequestErrorResponse> prepareExceptionResponse(Exception ex, HttpStatus status, Map<String,List<String>> errors) {

        customRequestErrorResponse.setTimestamp(new Date());
        customRequestErrorResponse.setStatus(status.value());
        customRequestErrorResponse.setErrors(errors);
        return new ResponseEntity<>(customRequestErrorResponse, status);
    }
}
