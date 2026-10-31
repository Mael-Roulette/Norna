package com.maelrltt.norna.mapper;

import com.maelrltt.norna.dto.boardColumn.BoardColumnResponse;
import com.maelrltt.norna.entity.BoardColumn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ColumnMapper {
    private final TaskMapper taskMapper;

    public BoardColumnResponse toResponse(BoardColumn boardColumn) {
        return BoardColumnResponse.builder()
                .id(boardColumn.getId())
                .name(boardColumn.getName())
                .position(boardColumn.getPosition())
                .tasks(boardColumn.getTasks().stream().map(taskMapper::toResponse).toList())
                .build();
    }
}
