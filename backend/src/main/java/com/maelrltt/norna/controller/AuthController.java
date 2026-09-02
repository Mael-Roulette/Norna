package com.maelrltt.norna.controller;

import com.maelrltt.norna.dto.UserRequest;
import com.maelrltt.norna.dto.UserResponse;
import com.maelrltt.norna.entity.User;
import com.maelrltt.norna.exception.EmailAlreadyExistsException;
import com.maelrltt.norna.exception.UsernameAlreadyExistsException;
import com.maelrltt.norna.repository.UserRepository;
import com.maelrltt.norna.security.JwtUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtility jwtUtility;

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
    public ResponseEntity<String> signIn(@RequestBody UserRequest userRequest) {
        // Runs the credentials through the AuthenticationProvider
        // Throws an exception if authentication fails
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.username(),
                        userRequest.password()
                )
        );

        // Principal is the authenticated user's details
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Issue a new JWT for this user
        assert userDetails != null;
        String token = jwtUtility.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(token);
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
    public ResponseEntity<UserResponse> signUp(@RequestBody UserRequest userRequest) {
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

        userRepository.save(newUser);

        // Map to a response DTO so the password hash never leaves the server
        UserResponse response = UserResponse.builder()
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .createdAt(newUser.getCreatedAt())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}