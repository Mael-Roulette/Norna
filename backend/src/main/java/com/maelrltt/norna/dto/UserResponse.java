package com.maelrltt.norna.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse (
    String username,
    String email,
    LocalDateTime createdAt
) {}
