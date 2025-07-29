package com.alexisindustries.krainet.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Schema(name = "AuthenticationResponse", description = "Response containing JWT access and refresh tokens.")
public record AuthenticationResponseDto(
        @Schema(description = "JWT access token used for authenticated requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String accessToken,
        @Schema(description = "JWT refresh token used to obtain new access tokens", example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...") String refreshToken) {

    public AuthenticationResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
