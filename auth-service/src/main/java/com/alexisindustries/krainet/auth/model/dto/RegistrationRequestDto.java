package com.alexisindustries.krainet.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Schema(name = "RegistrationRequest", description = "Request containing details required to register a new user.")
@Data
public class RegistrationRequestDto {

    @Schema(description = "Desired username for the new account", example = "jane_doe")
    private String username;

    @Schema(description = "Email address of the user", example = "jane.doe@example.com")
    private String email;

    @Schema(description = "First name of the user", example = "Jane")
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @Schema(description = "Password for the new account", example = "S3cur3P@ss!")
    private String password;
}
