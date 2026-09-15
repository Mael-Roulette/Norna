package com.maelrltt.norna.dto.auth;

import com.maelrltt.norna.dto.space.SpaceResponse;
import com.maelrltt.norna.entity.Space;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserResponse (
    UUID id,
    String username,
    String email,
    SpaceResponse lastVisitedSpace,
    LocalDateTime createdAt
) {}
