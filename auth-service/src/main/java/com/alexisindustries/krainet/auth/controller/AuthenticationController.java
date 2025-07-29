package com.alexisindustries.krainet.auth.controller;

import com.alexisindustries.krainet.auth.model.dto.AuthenticationResponseDto;
import com.alexisindustries.krainet.auth.model.dto.LoginRequestDto;
import com.alexisindustries.krainet.auth.model.dto.RegistrationRequestDto;
import com.alexisindustries.krainet.auth.service.AuthenticationService;
import com.alexisindustries.krainet.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Tag(name = "Authentication", description = "Endpoints for user registration, login, and token management")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided username, email, and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Username or email already in use",
                    content = @Content(mediaType = "text/plain"))
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Registration details: username, email, and password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegistrationRequestDto.class)
                    )
            )
            @RequestBody RegistrationRequestDto registrationDto
    ) {
        if (userService.existsByUsername(registrationDto.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        if (userService.existsByEmail(registrationDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        authenticationService.register(registrationDto);
        return ResponseEntity.ok("Registration successful");
    }

    @Operation(summary = "Authenticate user", description = "Authenticates user credentials and returns JWT tokens.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login details: username and password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequestDto.class)
                    )
            )
            @RequestBody LoginRequestDto request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @Operation(summary = "Refresh JWT token", description = "Refreshes the access token using a valid refresh token from the request bearer token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token invalid or expired", content = @Content)
    })
    @PostMapping("/refresh_token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authenticationService.refreshToken(request, response);
    }
}
