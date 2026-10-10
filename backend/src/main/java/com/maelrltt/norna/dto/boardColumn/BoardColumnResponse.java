package com.maelrltt.norna.dto.boardColumn;

import java.util.UUID;

public record BoardColumnResponse(
        UUID id,
        String name,
        Integer position
) {
}
