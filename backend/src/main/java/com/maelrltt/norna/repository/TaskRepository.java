package com.maelrltt.norna.repository;

import com.maelrltt.norna.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    // Retrieve tasks from a specific column
    List<Task> findByColumnIdOrderByPositionAsc(UUID columnId);

    // Retrieve all tasks from a specific board
    List<Task> findByColumnBoardIdOrderByColumnPositionAscPositionAsc(UUID boardId);

    @Query("select coalesce(max(t.position), -1) from Task t where t.column.id = :columnId")
    int findMaxPositionByColumnId(@Param("columnId") UUID columnId);
}
