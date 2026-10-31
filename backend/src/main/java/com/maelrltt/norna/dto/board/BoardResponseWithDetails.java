package com.maelrltt.norna.dto.board;

import com.maelrltt.norna.dto.boardColumn.BoardColumnResponse;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record BoardResponseWithDetails(
        UUID id,
        String name,
        String description,
        List<BoardColumnResponse> columns
) {
}
