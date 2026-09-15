package com.maelrltt.norna.dto.space;

import com.maelrltt.norna.entity.SpaceMember;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateSpaceRequest(
        @NotBlank( message = "Space name cannot be empty" )
        @Size( min = 4, max = 30, message = "Space name must be between 4 and 30 chars.")
        String name
) {
}
