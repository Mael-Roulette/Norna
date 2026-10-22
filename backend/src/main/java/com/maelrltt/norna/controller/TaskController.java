package com.maelrltt.norna.controller;

import com.maelrltt.norna.dto.task.TaskRequest;
import com.maelrltt.norna.dto.task.TaskResponse;
import com.maelrltt.norna.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            Authentication authentication,
            @Valid @RequestBody TaskRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.createTask(authentication, request));
    }
}
