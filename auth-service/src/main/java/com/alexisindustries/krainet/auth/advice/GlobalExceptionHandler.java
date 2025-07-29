package com.alexisindustries.krainet.auth.advice;

import com.alexisindustries.krainet.auth.model.dto.ErrorResponseDto;
import com.alexisindustries.krainet.auth.util.AuthException;
import com.alexisindustries.krainet.auth.util.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({UserException.class, AuthException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleExceptions(Exception e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}