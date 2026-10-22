package com.maelrltt.norna.mapper;

import com.maelrltt.norna.dto.task.TaskRequest;
import com.maelrltt.norna.dto.task.TaskResponse;
import com.maelrltt.norna.entity.BoardColumn;
import com.maelrltt.norna.entity.Task;
import com.maelrltt.norna.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    private final UserMapper userMapper;

    public Task toEntity(TaskRequest request, BoardColumn column, List<User> assignees) {
        return Task.builder()
                .title(request.title())
                .description(request.description())
                .dueDate(request.dueDate())
                .assignedUsers(assignees)
                .column(column)
                .build();
    }

    public TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.getCompleted())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .dueDate(task.getDueDate())
                .completedAt(task.getCompletedAt())
                .assignedUsers(task.getAssignedUsers().stream()
                        .map(userMapper::toResponse)
                        .toList())
                .column(task.getColumn())
                .build();
    }
}