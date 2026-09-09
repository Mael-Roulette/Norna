package com.maelrltt.norna.controller;

import com.maelrltt.norna.dto.AuthResponse;
import com.maelrltt.norna.dto.SignInUserRequest;
import com.maelrltt.norna.dto.UserRequest;
import com.maelrltt.norna.dto.UserResponse;
import com.maelrltt.norna.entity.User;
import com.maelrltt.norna.exception.EmailAlreadyExistsException;
import com.maelrltt.norna.exception.InvalidCredentialsException;
import com.maelrltt.norna.exception.UsernameAlreadyExistsException;
import com.maelrltt.norna.repository.UserRepository;
import com.maelrltt.norna.security.JwtUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtility jwtUtility;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtility jwtUtility
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtility = jwtUtility;
    }


    /**
     * Authenticates a user with username/password and returns a signed JWT.
     * @param userRequest contains the username and password to verify
     * @return 200 OK with the JWT as the response body
     */
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody SignInUserRequest userRequest) {
        String username = userRepository
                .findUsernameByEmail(userRequest.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            userRequest.password()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = jwtUtility.generateToken(Objects.requireNonNull(userDetails).getUsername());

            AuthResponse response = new AuthResponse(token, jwtExpiration);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    /**
     * Registers a new user account.
     * Validates that neither the username nor the email is already taken,
     * hashes the raw password before persisting it, and returns a
     * sanitized UserResponse (without the password) on success
     *
     * @param userRequest - contains the desired username, email, and raw password
     * @return 201 CREATED with the new user's public info or 409 CONFLICT if the username/email is already in use
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.username())) {
            throw new UsernameAlreadyExistsException("Username is already in use");
        }

        if (userRepository.existsByEmail(userRequest.email())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        // Build the entity to persist
        // The password is hashed here
        final User newUser = User.builder()
                .username(userRequest.username())
                .email(userRequest.email())
                .password(passwordEncoder.encode(userRequest.password()))
                .build();

        try {
            userRepository.save(newUser);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}