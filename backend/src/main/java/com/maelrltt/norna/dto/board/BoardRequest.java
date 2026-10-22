package com.maelrltt.norna.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record BoardRequest(
        @NotBlank(message = "Name cannot be empty")
        @Size( min = 0, max = 30, message = "Board name must be between 4 and 30 chars.")
        String name,

        String description
) {
}
