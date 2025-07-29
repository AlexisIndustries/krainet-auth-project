package com.alexisindustries.krainet.auth.service;

import com.alexisindustries.krainet.auth.model.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
public interface JwtService {
    boolean isValid(String token, UserDetails user);
    String extractUsername(String token);
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    boolean isValidRefresh(String token, User user);
}
