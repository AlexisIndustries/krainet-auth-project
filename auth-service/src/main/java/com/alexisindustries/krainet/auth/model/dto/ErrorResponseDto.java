package com.alexisindustries.krainet.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Schema(name = "ErrorResponse", description = "Standard error response containing an error message and timestamp.")
public record ErrorResponseDto(

        @Schema(description = "Description of the error", example = "User not found")
        String error,

        @Schema(description = "Timestamp when the error occurred", example = "2025-07-28T15:30:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp
) {}
