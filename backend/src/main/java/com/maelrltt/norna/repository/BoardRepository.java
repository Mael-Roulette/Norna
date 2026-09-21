package com.maelrltt.norna.repository;

import com.maelrltt.norna.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
    Optional<Board> findByIdAndSpaceId(UUID boardId, UUID spaceId);

    List<Board> findAllBySpaceId(UUID spaceId);
}
