package com.maelrltt.norna.dto.spaceMember;

import com.maelrltt.norna.entity.SpaceRole;

import java.util.UUID;

public record SpaceMemberResponse(
        UUID id,
        SpaceRole role
) {
}
