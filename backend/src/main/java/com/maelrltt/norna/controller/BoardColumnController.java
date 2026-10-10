package com.maelrltt.norna.controller;

import com.maelrltt.norna.dto.boardColumn.BoardColumnRequest;
import com.maelrltt.norna.dto.boardColumn.BoardColumnResponse;
import com.maelrltt.norna.service.BoardColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/board/{boardId}/column")
@RequiredArgsConstructor
public class BoardColumnController {
    private final BoardColumnService boardColumnService;

    @PostMapping
    public ResponseEntity<BoardColumnResponse> createColumn (
            Authentication authentication,
            @PathVariable UUID boardId,
            @RequestBody BoardColumnRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(boardColumnService.createBoardColumn(
                        authentication,
                        boardId,
                        request
                ));
    }

    @PatchMapping("/{columnId}")
    public ResponseEntity<BoardColumnResponse> updateColumn (
            Authentication authentication,
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @RequestBody BoardColumnRequest request
    ) {
        return ResponseEntity.ok(boardColumnService.updateBoardColumn(
                authentication,
                boardId,
                columnId,
                request
        ));
    }

    @DeleteMapping("/{columnId}")
    public ResponseEntity<BoardColumnResponse> deleteColumn (
            Authentication authentication,
            @PathVariable UUID boardId,
            @PathVariable UUID columnId
    ) {
        boardColumnService.deleteBoardColumn(
                authentication,
                boardId,
                columnId
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BoardColumnResponse>> getBoardColumns (
            Authentication authentication,
            @PathVariable UUID boardId
    ) {
        return ResponseEntity.ok(boardColumnService.getBoardColumns(
                authentication,
                boardId
        ));
    }
}
