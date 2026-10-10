package com.maelrltt.norna.dto.boardColumn;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateColumnNameRequest(
        @NotBlank
        @Size(max = 100)
        String name
) {
}
