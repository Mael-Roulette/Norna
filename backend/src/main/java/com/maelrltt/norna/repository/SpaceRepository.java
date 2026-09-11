package com.maelrltt.norna.repository;

import com.maelrltt.norna.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpaceRepository extends JpaRepository<Space, UUID> {
}
