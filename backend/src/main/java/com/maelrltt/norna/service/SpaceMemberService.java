package com.maelrltt.norna.service;

import com.maelrltt.norna.entity.Space;
import com.maelrltt.norna.entity.SpaceMember;
import com.maelrltt.norna.entity.SpaceRole;
import com.maelrltt.norna.entity.User;
import com.maelrltt.norna.repository.SpaceMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpaceMemberService {
    private final SpaceMemberRepository spaceMemberRepository;

    public void checkMembership(UUID spaceId, UUID userId) {
        if (!spaceMemberRepository.existsBySpaceIdAndUserId(spaceId, userId)) {
            throw new AccessDeniedException("User is not a member of this space");
        }
    }

    public List<SpaceMember> getMembershipsForUser(UUID userId) {
        return spaceMemberRepository.findByUserId(userId);
    }

    @Transactional
    public SpaceMember addMember(Space space, User user, SpaceRole role) {
        SpaceMember member = SpaceMember.builder()
                .space(space)
                .user(user)
                .role(role)
                .build();

        return spaceMemberRepository.save(member);
    }
}
