package com.alexisindustries.krainet.auth.controller;

import com.alexisindustries.krainet.auth.model.dto.UpdateUserDto;
import com.alexisindustries.krainet.auth.model.dto.UserMeDto;
import com.alexisindustries.krainet.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Tag(name = "Users", description = "Endpoints for retrieving and managing the authenticated user's profile")
@RestController
@RequestMapping("/api/user")
@Secured({"ADMIN", "USER"})
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get current user profile",
            description = "Returns the profile information of the currently authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserMeDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT token", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<UserMeDto> me() {
        return new ResponseEntity<>(userService.me(), HttpStatus.OK);
    }

    @Operation(summary = "Update current user profile",
            description = "Updates the profile details of the currently authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT token", content = @Content)
    })
    @PutMapping
    public ResponseEntity<HttpStatus> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated user details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUserDto.class)
                    )
            )
            @RequestBody UpdateUserDto updateUserDTO
    ) {
        userService.update(updateUserDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete current user",
            description = "Deletes the account of the currently authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT token", content = @Content)
    })
    @DeleteMapping
    public ResponseEntity<HttpStatus> delete() {
        userService.delete();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

