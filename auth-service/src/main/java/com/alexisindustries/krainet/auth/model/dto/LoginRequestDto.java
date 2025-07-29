package com.alexisindustries.krainet.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Schema(name = "LoginRequest", description = "Request containing user credentials for authentication.")
@Data
public class LoginRequestDto {

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Schema(description = "Password of the user", example = "P@ssw0rd!")
    private String password;
}
