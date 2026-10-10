package com.maelrltt.norna.dto.boardColumn;

import jakarta.validation.constraints.Min;

public record UpdateColumnPositionRequest(
        @Min(0)
        Integer position
) {
}
