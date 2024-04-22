package com.example.auth.controllers;

import com.example.auth.dtos.AuthenticationDTO;
import com.example.auth.dtos.LoginResponseDTO;
import com.example.auth.dtos.RegisterDTO;
import com.example.auth.entities.User;
import com.example.auth.exceptions.UserAlreadyExistsException;
import com.example.auth.security.TokenService;
import com.example.auth.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authDto, HttpSession session) {
        User user = authService.login(authDto);
        String token = tokenService.generateToken(user);

        // Salva o token na sessão
        session.setAttribute("token", token);
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Register successful"),
            @ApiResponse(responseCode = "401", description = "Invalid register")
    })
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        try {
            authService.register(data);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("User with this login already exists.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Inválida a sessão
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session not found");
        }
    }

    @ControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(UserAlreadyExistsException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}

