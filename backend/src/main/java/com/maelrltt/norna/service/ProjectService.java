package com.maelrltt.norna.service;

import com.maelrltt.norna.dto.board.BoardResponse;
import com.maelrltt.norna.dto.project.ProjectBoardsUpdateRequest;
import com.maelrltt.norna.dto.project.ProjectNameUpdateRequest;
import com.maelrltt.norna.dto.project.ProjectRequest;
import com.maelrltt.norna.dto.project.ProjectResponse;
import com.maelrltt.norna.entity.*;
import com.maelrltt.norna.exception.ResourceNotFoundException;
import com.maelrltt.norna.repository.BoardRepository;
import com.maelrltt.norna.repository.ProjectRepository;
import com.maelrltt.norna.repository.SpaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final AuthService authService;
    private final SpaceMemberService spaceMemberService;
    private final SpaceRepository spaceRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public ProjectResponse createProject(
            Authentication authentication,
            UUID spaceId,
            ProjectRequest projectRequest
    ) {
        User currentUser = authService.getCurrentUser(authentication);

        spaceMemberService.checkRole(spaceId, currentUser.getId(), SpaceRole.OWNER);

        Space space = spaceRepository.getSpaceById(spaceId);

        Project project = Project.builder()
                .name(projectRequest.name())
                .space(space)
                .build();
        projectRepository.save(project);

        List<Board> boards = boardRepository.findAllById(projectRequest.boardsId());
        for (Board board : boards) {
            if (!board.getSpace().getId().equals(spaceId)) {
                throw new ResourceNotFoundException("Board not found in this space");
            }
            board.setProject(project);
        }
        boardRepository.saveAll(boards);

        return new ProjectResponse(project.getId(), project.getName(),
                boards.stream().map(b -> new BoardResponse(b.getId(), b.getName(), b.getDescription())).toList());
    }

    public List<ProjectResponse> getProjects(
            Authentication authentication,
            UUID spaceId
    ) {
        User currentUser = authService.getCurrentUser(authentication);

        spaceMemberService.checkRole(
                spaceId,
                currentUser.getId(),
                SpaceRole.MEMBER,
                SpaceRole.OWNER
        );

        List<Project>  projects = projectRepository.findAllBySpaceId(spaceId);

        return projects.stream()
                .map(project -> new ProjectResponse(
                        project.getId(),
                        project.getName(),
                        project.getBoards().stream()
                                .map(b -> new BoardResponse(b.getId(), b.getName(), b.getDescription()))
                                .toList()
                )).toList();
    }

    @Transactional
    public ProjectResponse updateProjectName(
            Authentication authentication,
            UUID spaceId,
            UUID projectId,
            ProjectNameUpdateRequest projectNameUpdateRequest
    ) {
        User currentUser = authService.getCurrentUser(authentication);

        spaceMemberService.checkRole(spaceId, currentUser.getId(), SpaceRole.OWNER);

        Project project = projectRepository
                .findByIdAndSpaceId(projectId, spaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        project.setName(projectNameUpdateRequest.name());

        return new ProjectResponse(
                        project.getId(),
                        project.getName(),
                        project.getBoards().stream()
                                .map(b -> new BoardResponse(b.getId(), b.getName(), b.getDescription()))
                                .toList()
                );
    }

    @Transactional
    public ProjectResponse updateProjectBoards(
            Authentication authentication,
            UUID spaceId,
            UUID projectId,
            ProjectBoardsUpdateRequest projectBoardsUpdateRequest
    ) {
        User currentUser = authService.getCurrentUser(authentication);

        spaceMemberService.checkRole(spaceId, currentUser.getId(), SpaceRole.OWNER);

        Project project = projectRepository
                .findByIdAndSpaceId(projectId, spaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        List<Board> newBoards = boardRepository.findAllById(projectBoardsUpdateRequest.boardIds());

        if (newBoards.size() != projectBoardsUpdateRequest.boardIds().size()) {
            throw new ResourceNotFoundException("One or more boards not found");
        }

        for (Board board : newBoards) {
            if (!board.getSpace().getId().equals(spaceId)) {
                throw new ResourceNotFoundException("Board not found in this space");
            }
        }

        // Remove boards
        List<Board> currentBoards = boardRepository.findAllByProjectId(projectId);
        for (Board board : currentBoards) {
            if (!newBoards.contains(board)) {
                board.setProject(null);
            }
        }

        // Add new boards to the project
        for (Board board : newBoards) {
            board.setProject(project);
        }

        boardRepository.saveAll(currentBoards);
        boardRepository.saveAll(newBoards);

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                newBoards.stream()
                        .map(b -> new BoardResponse(b.getId(), b.getName(), b.getDescription()))
                        .toList()
        );
    }

    @Transactional
    public void deleteProject(
            Authentication authentication,
            UUID spaceId,
            UUID projectId
    ) {
        User currentUser = authService.getCurrentUser(authentication);

        spaceMemberService.checkRole(spaceId, currentUser.getId(), SpaceRole.OWNER);

        Project project = projectRepository
                .findByIdAndSpaceId(projectId, spaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // Remove boards
        List<Board> boards = boardRepository.findAllByProjectId(projectId);
        for (Board board : boards) {
            board.setProject(null);
        }
        boardRepository.saveAll(boards);

        projectRepository.delete(project);
    }
}
