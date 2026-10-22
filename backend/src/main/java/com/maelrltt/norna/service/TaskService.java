package com.maelrltt.norna.service;

import com.maelrltt.norna.dto.task.TaskRequest;
import com.maelrltt.norna.dto.task.TaskResponse;
import com.maelrltt.norna.entity.*;
import com.maelrltt.norna.exception.ResourceNotFoundException;
import com.maelrltt.norna.mapper.TaskMapper;
import com.maelrltt.norna.repository.BoardColumnRepository;
import com.maelrltt.norna.repository.TaskRepository;
import com.maelrltt.norna.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final AuthService authService;
    private final SpaceMemberService spaceMemberService;
    private final TaskMapper taskMapper;
    private final BoardColumnRepository  boardColumnRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskResponse createTask(
            Authentication authentication,
            TaskRequest request
    ) {
        User currentUser = authService.getCurrentUser(authentication);
        BoardColumn column = boardColumnRepository.findById(request.columnId())
                .orElseThrow(() -> new ResourceNotFoundException("Column not found"));

        spaceMemberService.checkRole(
                column.getBoard().getSpace().getId(),
                currentUser.getId(),
                SpaceRole.OWNER,
                SpaceRole.MEMBER
        );

        List<User> assignees = List.of();
        if (request.assignedUserIds() != null && !request.assignedUserIds().isEmpty()) {
            spaceMemberService.checkAllMembers(column.getBoard().getSpace().getId(), request.assignedUserIds());
            assignees = userRepository.findAllById(new HashSet<>(request.assignedUserIds()));
        }

        Task task = taskMapper.toEntity(request, column, assignees);
        Task saved = taskRepository.save(task);
        return taskMapper.toResponse(saved);
    }
}
