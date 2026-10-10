package com.maelrltt.norna.repository;

import com.maelrltt.norna.entity.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, UUID> {
    List<BoardColumn> findAllByBoardId( UUID boardId );
}
