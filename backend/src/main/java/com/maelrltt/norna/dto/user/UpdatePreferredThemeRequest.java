package com.maelrltt.norna.dto.user;

import com.maelrltt.norna.entity.AppTheme;
import jakarta.validation.constraints.NotBlank;

public record UpdatePreferredThemeRequest(
        @NotBlank(message = "Preferred theme cannot be empty")
        AppTheme newPreferredTheme
) {
}
