package com.maelrltt.norna.repository;

import com.maelrltt.norna.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    // Retrieve tasks from a specific column
    List<Task> findByColumnIdOrderByPositionAsc(UUID columnId);

    // Retrieve all tasks from a specific board
    List<Task> findByColumnBoardIdOrderByColumnPositionAscPositionAsc(UUID boardId);
}
