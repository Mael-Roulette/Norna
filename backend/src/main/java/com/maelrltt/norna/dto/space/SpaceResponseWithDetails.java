package com.maelrltt.norna.dto.space;

import com.maelrltt.norna.dto.spaceMember.SpaceMemberDetailsResponse;
import com.maelrltt.norna.dto.spaceMember.SpaceMemberResponse;

import java.util.List;
import java.util.UUID;

public record SpaceResponseWithDetails(
        UUID spaceId,
        String name,
        List<SpaceMemberDetailsResponse> members) {
}
