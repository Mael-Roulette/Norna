package com.maelrltt.norna.mapper;

import com.maelrltt.norna.dto.board.BoardResponse;
import com.maelrltt.norna.dto.board.BoardResponseWithDetails;
import com.maelrltt.norna.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardMapper {
    private final ColumnMapper columnMapper;

    public BoardResponseWithDetails toResponseWithDetails(Board board) {
        return BoardResponseWithDetails.builder()
                .id(board.getId())
                .name(board.getName())
                .description(board.getDescription())
                .columns(board.getColumns().stream().map(columnMapper::toResponse).toList())
                .build();
    }

    public BoardResponse toResponse(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .description(board.getDescription())
                .build();
    }
}
