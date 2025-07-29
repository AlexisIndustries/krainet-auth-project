package com.alexisindustries.krainet.auth.controller;

import com.alexisindustries.krainet.auth.model.dto.CreateUserDto;
import com.alexisindustries.krainet.auth.model.dto.UpdateUserDto;
import com.alexisindustries.krainet.auth.model.dto.UserDto;
import com.alexisindustries.krainet.auth.service.UserService;
import com.alexisindustries.krainet.auth.util.UserException;
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

import java.util.List;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Tag(name = "Admin Users", description = "Admin endpoints for creating, listing, and managing users")
@RestController
@RequestMapping("/api/v1/admin/users")
@Secured("ADMIN")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user account with the provided details.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Username or email already in use",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "Unauthorized - requires ADMIN role", content = @Content)
    })
    @PostMapping
    public ResponseEntity<String> create(
            @RequestBody(
                    required = true
            ) CreateUserDto createUserDto
    ) {
        if (userService.existsByUsername(createUserDto.username())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        if (userService.existsByEmail(createUserDto.email())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        userService.create(createUserDto);
        return ResponseEntity.ok("Registration successful");
    }

    @Operation(
            summary = "List users",
            description = "Retrieves a paginated list of users.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - requires ADMIN role", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return new ResponseEntity<>(userService.findAll(page, size), HttpStatus.OK);
    }

    @Operation(
            summary = "Find user by username or email",
            description = "Retrieves a single user by username or email.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Username or email is required", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - requires ADMIN role", content = @Content)
    })
    @GetMapping("/find")
    public ResponseEntity<UserDto> find(
            @RequestParam String username,
            @RequestParam String email
    ) {
        if (username != null) {
            return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
        } else if (email != null) {
            return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.OK);
        } else {
            throw new UserException("Username or email is required");
        }
    }

    @Operation(
            summary = "Update user by ID",
            description = "Updates the details of a user identified by ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - requires ADMIN role", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(
            @PathVariable long id,
            @RequestBody
            UpdateUserDto updateUserDto
    ) {
        userService.update(id, updateUserDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Delete user by ID",
            description = "Deletes a user identified by ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - requires ADMIN role", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable long id
    ) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
