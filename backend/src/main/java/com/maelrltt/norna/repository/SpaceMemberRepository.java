package com.maelrltt.norna.repository;

import com.maelrltt.norna.entity.SpaceMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpaceMemberRepository extends JpaRepository<SpaceMember, UUID> {
    boolean existsBySpaceIdAndUserId(UUID spaceId, UUID userId);

    Optional<SpaceMember> findBySpaceIdAndUserId(UUID spaceId, UUID userId);

    List<SpaceMember> findByUserId(UUID userId);
}
