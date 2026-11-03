package com.maelrltt.norna.dto.spaceMember;

import com.maelrltt.norna.entity.SpaceRole;

import java.util.UUID;

public record SpaceMemberDetailsResponse(
        UUID id,
        UUID userId,
        String username,
        String email,
        SpaceRole role
) {
}
