package com.maelrltt.norna.dto.project;

import jakarta.validation.constraints.NotBlank;

public record ProjectNameUpdateRequest(
        @NotBlank(message = "Project name cannot be empty.")
        String name
) {
}
