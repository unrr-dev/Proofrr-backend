package com.proofrr.proofrr.controller;

import com.proofrr.proofrr.dto.AuthResponse;
import com.proofrr.proofrr.dto.GoogleAuthRequest;
import com.proofrr.proofrr.dto.LoginRequest;
import com.proofrr.proofrr.dto.SignupRequest;
import com.proofrr.proofrr.dto.UserDto;
import com.proofrr.proofrr.model.User;
import com.proofrr.proofrr.security.JwtService;
import com.proofrr.proofrr.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Email/password and Google sign-in")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Create an account with email/password")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        User user = userService.register(request);
        String token = jwtService.generateToken(user);
        log.info("Issued JWT for signup user={}", user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse("Account created", UserDto.from(user), token));
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email/password")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request);
        String token = jwtService.generateToken(user);
        log.info("Issued JWT for login user={}", user.getEmail());
        return ResponseEntity.ok(new AuthResponse("Login successful", UserDto.from(user), token));
    }

    @PostMapping("/google")
    @Operation(summary = "Login with Google ID token", description = "Send an ID token from Google One Tap or OAuth")
    public ResponseEntity<AuthResponse> loginWithGoogle(@Valid @RequestBody GoogleAuthRequest request) {
        User user = userService.loginWithGoogle(request);
        String token = jwtService.generateToken(user);
        log.info("Issued JWT for google login user={}", user.getEmail());
        return ResponseEntity.ok(new AuthResponse("Google sign in successful", UserDto.from(user), token));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AuthResponse> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new AuthResponse(ex.getMessage(), null));
    }
}
