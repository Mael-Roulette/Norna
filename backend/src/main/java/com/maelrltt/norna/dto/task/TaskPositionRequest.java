package com.maelrltt.norna.dto.task;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record TaskPositionRequest(
        @NotBlank(message = "Position cannot be empty")
        Integer position,

        @NotBlank(message = "Column id cannot be empty")
        UUID columnId
) {
}
