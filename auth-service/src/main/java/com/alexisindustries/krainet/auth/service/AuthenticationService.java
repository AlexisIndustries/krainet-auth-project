package com.alexisindustries.krainet.auth.service;

import com.alexisindustries.krainet.auth.model.dto.AuthenticationResponseDto;
import com.alexisindustries.krainet.auth.model.dto.LoginRequestDto;
import com.alexisindustries.krainet.auth.model.dto.RegistrationRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
public interface AuthenticationService {
    ResponseEntity<AuthenticationResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response);
    AuthenticationResponseDto authenticate(LoginRequestDto request);
    void register(RegistrationRequestDto request);
}
