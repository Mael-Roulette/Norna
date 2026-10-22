package com.maelrltt.norna.dto.board;

import java.util.UUID;

public record BoardResponse(
        UUID id,
        String name,
        String description
) {
}
