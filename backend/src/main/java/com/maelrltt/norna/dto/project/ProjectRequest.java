package com.maelrltt.norna.dto.project;

import com.maelrltt.norna.entity.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record ProjectRequest(
        @NotBlank(message = "Project name cannot be empty.")
        String name,

        List<UUID> boardsId
) {
}
