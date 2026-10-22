package com.maelrltt.norna.dto.task;

import com.maelrltt.norna.dto.auth.UserResponse;
import com.maelrltt.norna.entity.BoardColumn;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record TaskResponse(
        UUID id,
        String title,
        String description,
        Boolean completed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime dueDate,
        LocalDateTime completedAt,
        List<UserResponse> assignedUsers,
        BoardColumn column
) {
}
