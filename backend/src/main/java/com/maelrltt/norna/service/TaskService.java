package com.maelrltt.norna.service;

import com.maelrltt.norna.dto.task.TaskRequest;
import com.maelrltt.norna.dto.task.TaskResponse;
import com.maelrltt.norna.entity.*;
import com.maelrltt.norna.exception.ResourceNotFoundException;
import com.maelrltt.norna.mapper.TaskMapper;
import com.maelrltt.norna.repository.BoardColumnRepository;
import com.maelrltt.norna.repository.BoardRepository;
import com.maelrltt.norna.repository.TaskRepository;
import com.maelrltt.norna.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

        int nextPosition = taskRepository.findMaxPositionByColumnId(column.getId()) + 1;

        Task task = taskMapper.toEntity(request, column, assignees);
        task.setPosition(nextPosition);

        Task saved = taskRepository.save(task);
        return taskMapper.toResponse(saved);
    }

    @Transactional
    public TaskResponse updateTask(
            Authentication authentication,
            UUID taskId,
            TaskRequest request
    ) {
        User currentUser = authService.getCurrentUser(authentication);
        Task currentTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        BoardColumn column = currentTask.getColumn();

        spaceMemberService.checkRole(
                column.getBoard().getSpace().getId(),
                currentUser.getId(),
                SpaceRole.OWNER,
                SpaceRole.MEMBER
        );

        if ( request.title() != null && !request.title().isEmpty() ) {
            currentTask.setTitle(request.title());
        }

        if ( request.description() != null && !request.description().isEmpty() ) {
            currentTask.setDescription(request.description());
        }

        if (request.completed() != null && !request.completed().equals(currentTask.getCompleted())) {
            currentTask.setCompleted(request.completed());
            currentTask.setCompletedAt(request.completed() ? LocalDateTime.now() : null);
        }

        if (request.dueDate() != null) {
            currentTask.setDueDate(request.dueDate());
        }

        if (request.assignedUserIds() != null) {
            Set<UUID> ids = new HashSet<>(request.assignedUserIds());
            List<User> users = userRepository.findAllById(ids);

            if (users.size() != ids.size()) {
                throw new ResourceNotFoundException("One or more users not found");
            }

            currentTask.getAssignedUsers().clear();
            currentTask.getAssignedUsers().addAll(users);
        }

        Task updatedTask = taskRepository.save(currentTask);
        return taskMapper.toResponse(updatedTask);
    }

    @Transactional
    public void deleteTask(
            Authentication authentication,
            UUID taskId
    ) {
        User currentUser = authService.getCurrentUser(authentication);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        BoardColumn column = task.getColumn();

        spaceMemberService.checkRole(
                column.getBoard().getSpace().getId(),
                currentUser.getId(),
                SpaceRole.OWNER,
                SpaceRole.MEMBER
        );

        UUID columnId = column.getId();
        int position = task.getPosition();

        taskRepository.delete(task);
        taskRepository.flush();
        taskRepository.shiftPositionsDown(columnId, position);
    }
}
