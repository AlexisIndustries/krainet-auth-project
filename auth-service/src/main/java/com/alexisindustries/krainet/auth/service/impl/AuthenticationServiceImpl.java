package com.alexisindustries.krainet.auth.service.impl;

import com.alexisindustries.krainet.auth.kafka.producer.NotificationProducer;
import com.alexisindustries.krainet.auth.mapper.UserMapper;
import com.alexisindustries.krainet.auth.model.Role;
import com.alexisindustries.krainet.auth.model.Token;
import com.alexisindustries.krainet.auth.model.User;
import com.alexisindustries.krainet.auth.model.dto.AuthenticationResponseDto;
import com.alexisindustries.krainet.auth.model.dto.LoginRequestDto;
import com.alexisindustries.krainet.auth.model.dto.RegistrationRequestDto;
import com.alexisindustries.krainet.auth.repository.TokenRepository;
import com.alexisindustries.krainet.auth.repository.UserRepository;
import com.alexisindustries.krainet.auth.service.AuthenticationService;
import com.alexisindustries.krainet.auth.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserMapper userMapper;

    private final NotificationProducer notificationProducer;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;

    public AuthenticationServiceImpl(UserMapper userMapper, NotificationProducer notificationProducer, UserRepository userRepository,
                                     JwtService jwtService,
                                     PasswordEncoder passwordEncoder,
                                     AuthenticationManager authenticationManager,
                                     TokenRepository tokenRepository) {
        this.userMapper = userMapper;
        this.notificationProducer = notificationProducer;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public void register(RegistrationRequestDto request) {
        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        notificationProducer.sendCreatedUserNotification(userMapper.toCreateUserDto(user));
        userRepository.save(user);
    }

    public AuthenticationResponseDto authenticate(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllToken(user);

        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponseDto(accessToken, refreshToken);
    }

    private void revokeAllToken(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokenByUser(user.getId());

        if(!validTokens.isEmpty()){
            validTokens.forEach(t -> t.setLoggedOut(true));
        }

        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();

        token.setAccessToken(accessToken);

        token.setRefreshToken(refreshToken);

        token.setLoggedOut(false);

        token.setUser(user);

        tokenRepository.save(token);
    }

    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorizationHeader.substring(7);

        String username = jwtService.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));

        if (jwtService.isValidRefresh(token, user)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllToken(user);

            saveUserToken(accessToken, refreshToken, user);

            return new ResponseEntity<>(new AuthenticationResponseDto(accessToken, refreshToken), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
