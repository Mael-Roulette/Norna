package com.maelrltt.norna.repository;

import com.maelrltt.norna.entity.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, UUID> {
}
