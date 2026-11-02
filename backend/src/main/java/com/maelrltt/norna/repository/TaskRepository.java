package com.maelrltt.norna.repository;

import com.maelrltt.norna.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    @Query("select coalesce(max(t.position), -1) from Task t where t.column.id = :columnId")
    int findMaxPositionByColumnId(@Param("columnId") UUID columnId);

    @Modifying(clearAutomatically = true)
    @Query("update Task t set t.position = t.position - 1 " +
            "where t.column.id = :columnId and t.position > :position")
    void shiftPositionsDown(@Param("columnId") UUID columnId, @Param("position") int position);
}
