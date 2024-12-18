package com.homeshare.controller;

import com.homeshare.domain.request.CreateUserRequest;
import com.homeshare.domain.request.LoginRequest;
import com.homeshare.domain.response.AuthResponse;
import com.homeshare.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/register")
    @Operation(summary = "Register a new provider", description = "Allows a new service provider to register.")
    public ResponseEntity<Void> registerProvider(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Provider registration details",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CreateUserRequest.class)
                    )
            )
            @Valid @RequestBody CreateUserRequest request) {
        securityService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user", description = "Allows a user to login and receive an access token.")
    public ResponseEntity<AuthResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login credentials",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LoginRequest.class)
                    )
            )
            @Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = securityService.login(request);
        return ResponseEntity.ok(authResponse);
    }
}
