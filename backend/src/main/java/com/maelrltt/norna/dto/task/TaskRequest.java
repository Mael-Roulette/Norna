package com.maelrltt.norna.dto.task;

import com.maelrltt.norna.entity.BoardColumn;
import com.maelrltt.norna.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TaskRequest(
        @NotBlank(message = "Title cannot be empty")
        @Size( min = 1, max = 30, message = "Task title must be between 1 and 30 chars.")
        String title,

        @Size( max = 500, message = "Description must be under 500 chars.")
        String description,
        LocalDateTime dueDate,
        List<UUID> assignedUserIds,

        @NotNull(message = "Column cannot be empty")
        UUID columnId
) {
}
