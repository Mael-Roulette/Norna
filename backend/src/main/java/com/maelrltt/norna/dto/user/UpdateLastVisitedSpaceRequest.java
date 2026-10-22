package com.maelrltt.norna.dto.user;

import java.util.UUID;

public record UpdateLastVisitedSpaceRequest(
        UUID spaceId
) {
}
