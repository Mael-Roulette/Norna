package com.maelrltt.norna.service;

import com.maelrltt.norna.dto.board.BoardRequest;
import com.maelrltt.norna.dto.board.BoardResponse;
import com.maelrltt.norna.entity.*;
import com.maelrltt.norna.exception.ResourceNotFoundException;
import com.maelrltt.norna.repository.BoardRepository;
import com.maelrltt.norna.repository.SpaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final AuthService authService;
    private final BoardRepository boardRepository;
    private final SpaceMemberService spaceMemberService;
    private final SpaceRepository spaceRepository;

    @Transactional
    public BoardResponse createBoard(
            Authentication authentication,
            UUID spaceId,
            BoardRequest boardRequest
    ) {
        User currentUser = authService.getCurrentUser(authentication);

        spaceMemberService.checkRole(
                spaceId,
                currentUser.getId(),
                SpaceRole.OWNER
        );

        Space space = spaceRepository.getSpaceById(spaceId);

        Board board = Board.builder()
                .name(boardRequest.name())
                .description(boardRequest.description())
                .space(space)
                .build();

        boardRepository.save(board);

        return new BoardResponse(
                board.getId(),
                board.getName(),
                board.getDescription()
        );
    }

    @Transactional
    public BoardResponse updateBoard(
            Authentication authentication,
            UUID spaceId,
            UUID boardId,
            BoardRequest boardRequest
    ) {
        User currentUser = authService.getCurrentUser(authentication);

        spaceMemberService.checkRole(
                spaceId,
                currentUser.getId(),
                SpaceRole.OWNER
        );

        Board board = boardRepository
                .findByIdAndSpaceId(boardId, spaceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Board not found")
                );

        if (boardRequest.name() != null && !boardRequest.name().isEmpty()) {
            board.setName(boardRequest.name());
        }

        board.setDescription(boardRequest.description());

        return new BoardResponse(
                board.getId(),
                board.getName(),
                board.getDescription()
        );
    }

    @Transactional
    public void deleteBoard(
            Authentication authentication,
            UUID spaceId,
            UUID boardId
    ) {
        User currentUser = authService.getCurrentUser(authentication);

        spaceMemberService.checkRole(
                spaceId,
                currentUser.getId(),
                SpaceRole.OWNER
        );

        Board board = boardRepository
                .findByIdAndSpaceId(boardId, spaceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Board not found")
                );

        boardRepository.delete(board);
    }

    public List<BoardResponse> getBoards(
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

        List<Board> boards = boardRepository.findAllBySpaceId(spaceId);

        return boards.stream()
                .map(board -> new BoardResponse(
                        board.getId(),
                        board.getName(),
                        board.getDescription()
                ))
                .toList();
    }
}