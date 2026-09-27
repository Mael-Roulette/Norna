package com.maelrltt.norna.service;

import com.maelrltt.norna.dto.auth.AuthResponse;
import com.maelrltt.norna.dto.auth.SignInUserRequest;
import com.maelrltt.norna.dto.auth.SignUpUserRequest;
import com.maelrltt.norna.dto.auth.UserResponse;
import com.maelrltt.norna.entity.User;
import com.maelrltt.norna.exception.EmailAlreadyExistsException;
import com.maelrltt.norna.exception.InvalidCredentialsException;
import com.maelrltt.norna.exception.UserNotFoundException;
import com.maelrltt.norna.exception.UsernameAlreadyExistsException;
import com.maelrltt.norna.mapper.UserMapper;
import com.maelrltt.norna.repository.UserRepository;
import com.maelrltt.norna.security.JwtUtility;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtility jwtUtility;
    private final DefaultDataService defaultDataService;
    private final UserMapper userMapper;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public AuthResponse signIn(SignInUserRequest signInUserRequest) {
        String username = userRepository
                .findUsernameByEmail(signInUserRequest.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            signInUserRequest.password()
                    )
            );

            User currentUser = getCurrentUser(authentication);

            String token = jwtUtility.generateToken(currentUser.getId().toString());
            String refreshToken = jwtUtility.generateRefreshToken(currentUser.getId().toString());

            return new AuthResponse(token, jwtExpiration, refreshToken);
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    @Transactional
    public void signUp (SignUpUserRequest signUpUserRequest) {
        if (userRepository.existsByUsername(signUpUserRequest.username())) {
            throw new UsernameAlreadyExistsException("Username is already in use");
        }

        if (userRepository.existsByEmail(signUpUserRequest.email())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        // Build the entity to persist
        // The password is hashed here
        final User newUser = User.builder()
                .username(signUpUserRequest.username())
                .email(signUpUserRequest.email())
                .password(passwordEncoder.encode(signUpUserRequest.password()))
                .build();

        userRepository.save(newUser);

        defaultDataService.createDefaultData(newUser);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserResponse getCurrentUserResponse(String username) {
        User user = getUserByUsername(username);

        return userMapper.toResponse(user);
    }

    public User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return getUserByUsername(username);
    }

    public ResponseCookie logout () {
        return ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/auth")
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtUtility.validateToken(refreshToken)
                || !jwtUtility.isRefreshToken(refreshToken)) {
            return null;
        }

        String userId = jwtUtility.getIdFromToken(refreshToken);
        String newAccessToken = jwtUtility.generateToken(userId);

        return new AuthResponse(
                newAccessToken,
                jwtExpiration,
                null
        );
    }
}
