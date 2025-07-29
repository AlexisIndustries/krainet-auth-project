package com.alexisindustries.krainet.auth.model.dto;

import com.alexisindustries.krainet.auth.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Schema(name = "CreateUser", description = "Request containing details to create a new user account.")
public record CreateUserDto(

        @Schema(description = "Desired username for the new account", example = "jane_doe")
        String username,

        @Schema(description = "Email address of the new user", example = "jane.doe@example.com")
        String email,

        @Schema(description = "Password for the new user account", example = "S3cur3P@ss!")
        String password,

        @Schema(description = "First name of the new user", example = "Jane")
        String firstName,

        @Schema(description = "Last name of the new user", example = "Doe")
        String lastName,

        @Schema(description = "User Role", example = "ADMIN")
        Role role
) {}
