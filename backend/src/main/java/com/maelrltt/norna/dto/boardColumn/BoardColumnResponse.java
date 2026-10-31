package com.maelrltt.norna.dto.boardColumn;

import com.maelrltt.norna.dto.task.TaskResponse;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record BoardColumnResponse(
        UUID id,
        String name,
        Integer position,
        List<TaskResponse> tasks
) {
}
