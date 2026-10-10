package com.maelrltt.norna.service;

import com.maelrltt.norna.dto.boardColumn.BoardColumnRequest;
import com.maelrltt.norna.dto.boardColumn.BoardColumnResponse;
import com.maelrltt.norna.entity.*;
import com.maelrltt.norna.repository.BoardColumnRepository;
import com.maelrltt.norna.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardColumnService {
    private final AuthService authService;
    private final SpaceMemberService spaceMemberService;
    private final BoardRepository boardRepository;
    private final BoardColumnRepository boardColumnRepository;

    @Transactional
    public BoardColumnResponse createBoardColumn (
            Authentication authentication,
            UUID boardId,
            BoardColumnRequest request
    ) {
        User currentUser = authService.getCurrentUser(authentication);
        Board board = boardRepository.getBoardById(boardId);

        spaceMemberService.checkRole(
                board.getSpace().getId(),
                currentUser.getId(),
                SpaceRole.OWNER,
                SpaceRole.MEMBER
        );

        BoardColumn column = BoardColumn.builder()
                .name(request.name())
                .position(request.position())
                .board(board)
                .build();

        boardColumnRepository.save(column);

        return new BoardColumnResponse(
                column.getId(),
                column.getName(),
                column.getPosition()
        );
    }

    @Transactional
    public BoardColumnResponse updateBoardColumn(
            Authentication authentication,
            UUID boardId,
            UUID columnId,
            BoardColumnRequest request
    ) {
        User currentUser = authService.getCurrentUser(authentication);
        Board board = boardRepository.getBoardById(boardId);
        BoardColumn column = boardColumnRepository.findById(columnId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Board column not found"
                ));

        if (!column.getBoard().getId().equals(board.getId())) {
            throw new EntityNotFoundException(
                    "Board column does not belong to this board"
            );
        }

        spaceMemberService.checkRole(
                board.getSpace().getId(),
                currentUser.getId(),
                SpaceRole.OWNER,
                SpaceRole.MEMBER
        );

        if (request.name() != null) {
            if (request.name().isBlank()) {
                throw new IllegalArgumentException(
                        "Column name cannot be blank"
                );
            }

            column.setName(request.name().trim());
        }

        if (request.position() != null) {
            if (request.position() < 0) {
                throw new IllegalArgumentException(
                        "Column position cannot be negative"
                );
            }

            column.setPosition(request.position());
        }

        return new BoardColumnResponse(
                column.getId(),
                column.getName(),
                column.getPosition()
        );
    }

    public void deleteBoardColumn (
            Authentication authentication,
            UUID boardId,
            UUID columnId
    ) {
        User currentUser = authService.getCurrentUser(authentication);
        Board board = boardRepository.getBoardById(boardId);
        BoardColumn column = boardColumnRepository.findById(columnId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Board column not found"
                ));

        if (!column.getBoard().getId().equals(board.getId())) {
            throw new EntityNotFoundException(
                    "Board column does not belong to this board"
            );
        }

        spaceMemberService.checkRole(
                board.getSpace().getId(),
                currentUser.getId(),
                SpaceRole.OWNER,
                SpaceRole.MEMBER
        );

        boardColumnRepository.delete(column);
    }

    public List<BoardColumnResponse> getBoardColumns (
            Authentication authentication,
            UUID boardId
    ) {
        User currentUser = authService.getCurrentUser(authentication);
        Board board = boardRepository.getBoardById(boardId);

        spaceMemberService.checkRole(
                board.getSpace().getId(),
                currentUser.getId(),
                SpaceRole.OWNER,
                SpaceRole.MEMBER
        );

        List<BoardColumn> columns = boardColumnRepository.findAllByBoardId( boardId );

        return columns.stream()
                .map(column -> new BoardColumnResponse(
                        column.getId(),
                        column.getName(),
                        column.getPosition()
                ))
                .toList();
    }
}
