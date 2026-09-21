package com.maelrltt.norna.controller;

import com.maelrltt.norna.dto.project.ProjectBoardsUpdateRequest;
import com.maelrltt.norna.dto.project.ProjectNameUpdateRequest;
import com.maelrltt.norna.dto.project.ProjectRequest;
import com.maelrltt.norna.dto.project.ProjectResponse;
import com.maelrltt.norna.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/space/{spaceId}/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            Authentication authentication,
            @PathVariable UUID spaceId,
            @RequestBody ProjectRequest projectRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.projectService.createProject(authentication, spaceId, projectRequest));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects(
            Authentication authentication,
            @PathVariable UUID spaceId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.projectService.getProjects(authentication, spaceId));
    }

    @PatchMapping("/{projectId}/name")
    public ResponseEntity<ProjectResponse> updateProjectName(
            Authentication authentication,
            @PathVariable UUID spaceId,
            @PathVariable UUID projectId,
            @RequestBody ProjectNameUpdateRequest projectNameUpdateRequest
    ) {
        return ResponseEntity.ok(
                projectService.updateProjectName(authentication, spaceId, projectId, projectNameUpdateRequest)
        );
    }

    @PatchMapping("/{projectId}/boards")
    public ResponseEntity<ProjectResponse> updateProjectBoards(
            Authentication authentication,
            @PathVariable UUID spaceId,
            @PathVariable UUID projectId,
            @RequestBody ProjectBoardsUpdateRequest projectBoardsUpdateRequest
    ) {
        return ResponseEntity.ok(
                projectService.updateProjectBoards(authentication, spaceId, projectId, projectBoardsUpdateRequest)
        );
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            Authentication authentication,
            @PathVariable UUID spaceId,
            @PathVariable UUID projectId
    ) {
        projectService.deleteProject(authentication, spaceId, projectId);
        return ResponseEntity.noContent().build();
    }
}
