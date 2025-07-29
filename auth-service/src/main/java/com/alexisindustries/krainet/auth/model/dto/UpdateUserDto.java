package com.alexisindustries.krainet.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Schema(name = "UpdateUser", description = "Request containing user details for updating an existing profile.")
public record UpdateUserDto(

        @Schema(description = "Unique username of the user", example = "john_doe")
        String username,

        @Schema(description = "Email address of the user", example = "john.doe@example.com")
        String email,

        @Schema(description = "Password for the user account", example = "NewP@ssw0rd!")
        String password,

        @Schema(description = "First name of the user", example = "John")
        String firstName,

        @Schema(description = "Last name of the user", example = "Doe")
        String lastName
) {}