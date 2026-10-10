package com.maelrltt.norna.service;

import com.maelrltt.norna.dto.board.BoardResponse;
import com.maelrltt.norna.dto.boardColumn.BoardColumnRequest;
import com.maelrltt.norna.dto.boardColumn.BoardColumnResponse;
import com.maelrltt.norna.entity.*;
import com.maelrltt.norna.repository.BoardColumnRepository;
import com.maelrltt.norna.repository.BoardRepository;
import com.maelrltt.norna.repository.SpaceRepository;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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

        return new BoardColumnResponse(
                column.getId(),
                column.getName(),
                column.getPosition()
        );
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
