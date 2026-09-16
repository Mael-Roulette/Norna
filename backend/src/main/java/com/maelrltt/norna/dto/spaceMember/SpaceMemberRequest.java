package com.maelrltt.norna.dto.spaceMember;

import com.maelrltt.norna.entity.SpaceRole;

import java.util.UUID;

public record SpaceMemberRequest(
        UUID userId,
        SpaceRole role
) {
}
