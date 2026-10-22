package com.maelrltt.norna.dto.auth;

import com.maelrltt.norna.dto.space.SpaceResponse;
import com.maelrltt.norna.entity.AppTheme;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserResponse (
    UUID id,
    String username,
    String email,
    AppTheme preferredTheme,
    UUID lastVisitedSpace,
    LocalDateTime createdAt,
    LocalDateTime updateAt
) {}
