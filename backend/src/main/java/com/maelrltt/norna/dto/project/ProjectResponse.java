package com.maelrltt.norna.dto.project;

import com.maelrltt.norna.dto.board.BoardResponse;
import com.maelrltt.norna.entity.Board;

import java.util.List;
import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        List<BoardResponse> boards
) {
}
