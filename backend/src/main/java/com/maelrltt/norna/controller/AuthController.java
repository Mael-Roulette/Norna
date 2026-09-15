package com.maelrltt.norna.controller;

import com.maelrltt.norna.dto.auth.AuthResponse;
import com.maelrltt.norna.dto.auth.SignInUserRequest;
import com.maelrltt.norna.dto.auth.SignUpUserRequest;
import com.maelrltt.norna.dto.auth.UserResponse;
import com.maelrltt.norna.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Authenticates a user with username/password and returns a signed JWT.
     * @param signInUserRequest contains the username and password to verify
     * @return 200 OK with the JWT as the response body
     */
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody SignInUserRequest signInUserRequest, HttpServletResponse response) {
        AuthResponse authResponse = this.authService.signIn(signInUserRequest);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", authResponse.refreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/api/v1/auth")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new AuthResponse(authResponse.token(), authResponse.expiresIn(), null));
    }

    /**
     * Registers a new user account.
     * Validates that neither the username nor the email is already taken,
     * hashes the raw password before persisting it, and returns a
     * sanitized UserResponse (without the password) on success
     *
     * @param signUpUserRequest - contains the desired username, email, and raw password
     * @return 201 CREATED or 409 CONFLICT if the username/email is already in use
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpUserRequest signUpUserRequest) {
        authService.signUp(signUpUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken (@CookieValue("refresh_token") String refreshToken) {
        AuthResponse authResponse = authService.refreshToken(refreshToken);

        if (authResponse == null) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Invalid refresh token");
        }

        return ResponseEntity.ok(authResponse);
    }

    /**
     * Get the user authenticated infos
     *
     * @return a userResponse so we can get user public infos
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(this.authService.getCurrentUser(authentication.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = this.authService.logout();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.noContent().build();
    }
}