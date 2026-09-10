package com.maelrltt.norna.dto.auth;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse (
    String username,
    String email,
    LocalDateTime createdAt
) {}
