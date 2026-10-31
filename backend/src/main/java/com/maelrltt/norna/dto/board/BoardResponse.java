package com.maelrltt.norna.dto.board;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BoardResponse(
        UUID id,
        String name,
        String description
) {
}
