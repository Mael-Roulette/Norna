package com.maelrltt.norna.dto;

public record AuthResponse(
        String token,
        int expiresIn
) { }
