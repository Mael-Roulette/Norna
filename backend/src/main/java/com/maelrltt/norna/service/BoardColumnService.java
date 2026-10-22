package com.maelrltt.norna.service;

import com.maelrltt.norna.dto.boardColumn.CreateBoardColumnRequest;
import com.maelrltt.norna.dto.boardColumn.BoardColumnResponse;
import com.maelrltt.norna.dto.boardColumn.UpdateColumnNameRequest;
import com.maelrltt.norna.dto.boardColumn.UpdateColumnPositionRequest;
import com.maelrltt.norna.entity.*;
import com.maelrltt.norna.repository.BoardColumnRepository;
import com.maelrltt.norna.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
            CreateBoardColumnRequest request
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
    public BoardColumnResponse updateColumnPosition(
            Authentication authentication,
            UUID boardId,
            UUID columnId,
            UpdateColumnPositionRequest request
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

        if (request.position() != null) {
            if (request.position() < 0) {
                throw new IllegalArgumentException(
                        "Column position cannot be negative"
                );
            }

            List<BoardColumn> columns = new ArrayList<>(
                    boardColumnRepository.findAllByBoardIdOrderByPositionAsc(boardId)
            );

            // We remove the column who need to be update then we add the column to her new place
            columns.removeIf(c -> c.getId().equals(column.getId()));
            int target = Math.min(request.position(), columns.size()); // borne max
            columns.add(target, column);

            // We set each column position
            for (int i = 0; i < columns.size(); i++) {
                columns.get(i).setPosition(i);
            }
        }

        return new BoardColumnResponse(
                column.getId(),
                column.getName(),
                column.getPosition()
        );
    }

    @Transactional
    public BoardColumnResponse updateColumnName(
            Authentication authentication,
            UUID boardId,
            UUID columnId,
            UpdateColumnNameRequest request
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

        List<BoardColumn> remaining = boardColumnRepository
                .findAllByBoardIdOrderByPositionAsc(boardId)
                .stream()
                .filter(c -> !c.getId().equals(column.getId()))
                .toList();

        for (int i = 0; i < remaining.size(); i++) {
            remaining.get(i).setPosition(i);
        }

        boardColumnRepository.saveAllAndFlush(remaining);
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
