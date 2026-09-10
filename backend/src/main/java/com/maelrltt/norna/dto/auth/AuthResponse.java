package com.maelrltt.norna.dto.auth;

public record AuthResponse(
        String token,
        int expiresIn,
        String refreshToken
) { }
