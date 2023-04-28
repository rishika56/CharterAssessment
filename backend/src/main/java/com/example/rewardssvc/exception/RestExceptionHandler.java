package com.example.rewardssvc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Exception handler for handling application errors
 *
 * @author MKANAKAL
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({NoSuchElementException.class})
    protected ResponseEntity<Object> handleNoSuchElementException(
            NoSuchElementException ex) {
        log.error("handling NoSuchElementException.", ex);
        ApiError apiError = new ApiError(NOT_FOUND, "No data found", ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleEntityNotFound(
            Exception ex) {
        log.error("handling Exception.", ex);
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex);
        return buildResponseEntity(apiError);
    }
}

