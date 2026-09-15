package com.maelrltt.norna.dto.space;

import com.maelrltt.norna.dto.spaceMember.SpaceMemberResponse;
import com.maelrltt.norna.entity.SpaceMember;

import java.util.List;
import java.util.UUID;

public record SpaceResponse(
        UUID spaceId,
        String name,
        List<SpaceMemberResponse> members
) {
}
