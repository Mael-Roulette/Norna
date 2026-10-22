package com.maelrltt.norna.repository;

import com.maelrltt.norna.entity.SpaceMember;
import com.maelrltt.norna.entity.SpaceRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpaceMemberRepository extends JpaRepository<SpaceMember, UUID> {
    boolean existsBySpaceIdAndUserId(UUID spaceId, UUID userId);

    Optional<SpaceMember> findBySpaceIdAndUserId(UUID spaceId, UUID userId);

    List<SpaceMember> findByUserId(UUID userId);

    void deleteBySpaceIdAndUserId(UUID spaceId, UUID userId);

    boolean existsBySpaceIdAndRoleAndUserIdNot(UUID spaceId, SpaceRole role, UUID userId);

    Optional<SpaceMember> findFirstBySpaceIdAndRoleAndUserIdNotOrderByJoinedAtAsc(
            UUID spaceId, SpaceRole role, UUID userId);

    @Query("""
        SELECT COUNT(sm) FROM SpaceMember sm
        WHERE sm.space.id = :spaceId AND sm.user.id IN :userIds
    """)
    long countBySpaceIdAndUserIdIn(
            @Param("spaceId") UUID spaceId,
            @Param("userIds") Collection<UUID> userIds
    );
}
