package com.maelrltt.norna.controller;

import com.maelrltt.norna.dto.boardColumn.CreateBoardColumnRequest;
import com.maelrltt.norna.dto.boardColumn.BoardColumnResponse;
import com.maelrltt.norna.dto.boardColumn.UpdateColumnNameRequest;
import com.maelrltt.norna.dto.boardColumn.UpdateColumnPositionRequest;
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
            @RequestBody CreateBoardColumnRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(boardColumnService.createBoardColumn(
                        authentication,
                        boardId,
                        request
                ));
    }

    @PatchMapping("/{columnId}/name")
    public ResponseEntity<BoardColumnResponse> updateColumnName (
            Authentication authentication,
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @RequestBody UpdateColumnNameRequest request
    ) {
        return ResponseEntity.ok(boardColumnService.updateColumnName(
                authentication,
                boardId,
                columnId,
                request
        ));
    }

    @PatchMapping("/{columnId}/position")
    public ResponseEntity<BoardColumnResponse> updateColumnPosition (
            Authentication authentication,
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @RequestBody UpdateColumnPositionRequest request
    ) {
        return ResponseEntity.ok(boardColumnService.updateColumnPosition(
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
