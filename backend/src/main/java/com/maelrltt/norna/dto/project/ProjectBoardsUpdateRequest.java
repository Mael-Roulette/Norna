package com.maelrltt.norna.dto.project;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record ProjectBoardsUpdateRequest(
        @NotEmpty(message = "Project must have at least one board.")
        List<UUID> boardIds
) {
}
