package com.maelrltt.norna.controller;

import com.maelrltt.norna.dto.board.BoardRequest;
import com.maelrltt.norna.dto.board.BoardResponse;
import com.maelrltt.norna.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/space/{spaceId}/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(
            Authentication authentication,
            @PathVariable UUID spaceId,
            @RequestBody BoardRequest boardRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(boardService.createBoard(
                        authentication,
                        spaceId,
                        boardRequest
                ));
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getBoards(
            Authentication authentication,
            @PathVariable UUID spaceId
    ) {
        return ResponseEntity.ok(
                boardService.getBoards(authentication, spaceId)
        );
    }

    @GetMapping("/{boardId")
    public ResponseEntity<BoardResponse> getBoard(
            Authentication authentication,
            @PathVariable UUID spaceId,
            @PathVariable UUID boardId
    ) {
        return ResponseEntity.ok(
                boardService.getBoardById(authentication, spaceId, boardId)
        );
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardResponse> updateBoard(
            Authentication authentication,
            @PathVariable UUID spaceId,
            @PathVariable UUID boardId,
            @RequestBody BoardRequest boardRequest
    ) {
        return ResponseEntity.ok(
                boardService.updateBoard(
                        authentication,
                        spaceId,
                        boardId,
                        boardRequest
                )
        );
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            Authentication authentication,
            @PathVariable UUID spaceId,
            @PathVariable UUID boardId
    ) {
        boardService.deleteBoard(
                authentication,
                spaceId,
                boardId
        );

        return ResponseEntity.noContent().build();
    }
}
