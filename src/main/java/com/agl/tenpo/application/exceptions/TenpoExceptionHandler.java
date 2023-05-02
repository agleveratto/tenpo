package com.agl.tenpo.application.exceptions;

import com.agl.tenpo.domain.exceptions.PercentageCachedNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class TenpoExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PercentageCachedNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(PercentageCachedNotFoundException exception, WebRequest request){

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Percentage not found into cache");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Object> handleConnectException(RestClientException exception, WebRequest request){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Internal Server Error");

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
