package com.maelrltt.norna.service;

import com.maelrltt.norna.entity.Space;
import com.maelrltt.norna.entity.SpaceMember;
import com.maelrltt.norna.entity.SpaceRole;
import com.maelrltt.norna.entity.User;
import com.maelrltt.norna.exception.MemberAlreadyExistsException;
import com.maelrltt.norna.exception.UserNotFoundException;
import com.maelrltt.norna.repository.SpaceMemberRepository;
import com.maelrltt.norna.repository.SpaceRepository;
import com.maelrltt.norna.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpaceMemberService {
    private final SpaceMemberRepository spaceMemberRepository;
    private final AuthService authService;
    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;

    /**
     * Verify is the user belongs to the space
     * @param spaceId ID of the wanted space
     * @param userId ID of the user who want to perform an action
     */
    public void checkMembership(UUID spaceId, UUID userId) {
        if (!spaceMemberRepository.existsBySpaceIdAndUserId(spaceId, userId)) {
            throw new AccessDeniedException("User is not a member of this space");
        }
    }

    /**
     * Verify if the user belongs to the space and possesses the required permissions to execute the action
     * @param spaceId ID of the wanted space
     * @param userId ID of the user who want to perform an action
     * @param allowedRoles Roles the user need to have to perform his action
     */
    public void checkRole(UUID spaceId, UUID userId, SpaceRole... allowedRoles) {
        SpaceMember member = spaceMemberRepository.findBySpaceIdAndUserId(spaceId, userId)
                .orElseThrow(() -> new AccessDeniedException("User is not a member of this space"));

        boolean hasRole = Arrays.stream(allowedRoles).anyMatch(role -> role == member.getRole());

        if (!hasRole) {
            throw new AccessDeniedException("User does not have the required role for this action");
        }
    }

    public List<SpaceMember> getMembershipsForUser(UUID userId) {
        return spaceMemberRepository.findByUserId(userId);
    }

    @Transactional
    public SpaceMember createOwner(Space space, User user) {
        SpaceMember owner = SpaceMember.builder()
                .space(space)
                .user(user)
                .role(SpaceRole.OWNER)
                .build();

        return spaceMemberRepository.save(owner);
    }

    @Transactional
    public SpaceMember addMember(UUID spaceId, UUID userId, SpaceRole role, Authentication authentication) {
        // Get the user who ask for the update
        User authenticatedUser = this.authService.getCurrentUser( authentication );

        // Only the owner can add a user
        this.checkRole(spaceId, authenticatedUser.getId(), SpaceRole.OWNER);

        // Check if the user is already a member of the space
        if (spaceMemberRepository.existsBySpaceIdAndUserId(spaceId, userId)) {
            throw new MemberAlreadyExistsException(
                    "User " + userId + " is already a member of space " + spaceId
            );
        }

        if (role == SpaceRole.OWNER) {
            throw new AccessDeniedException(
                    "You cannot assign the OWNER role when adding a member."
            );
        }

        Space space = this.spaceRepository.getSpaceById(spaceId);
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User " + userId + " not found")
        );

        SpaceMember member = SpaceMember.builder()
                .space(space)
                .user(user)
                .role(role)
                .build();

        return spaceMemberRepository.save(member);
    }

    @Transactional
    public SpaceMember updateMember(UUID spaceId, UUID userId, SpaceRole role, Authentication authentication) {
        // Get the user who ask for the update
        User authenticatedUser = this.authService.getCurrentUser( authentication );

        // Only the owner can update a user role
        this.checkRole(spaceId, authenticatedUser.getId(), SpaceRole.OWNER);

        if ( userId.equals(authenticatedUser.getId())) {
            throw new AccessDeniedException(
                    "You can't change your own role."
            );
        }

        // Verify that the user is part of the space
        this.checkMembership(spaceId, userId);

        // Get the member to update
        SpaceMember member = spaceMemberRepository
                .findBySpaceIdAndUserId(spaceId, userId)
                .orElseThrow(() ->
                        new AccessDeniedException("User is not a member of this space")
                );

        member.setRole(role);

        return spaceMemberRepository.save(member);
    }

    @Transactional
    public void removeMember(UUID spaceId, UUID userId, Authentication authentication) {
        // Get the user who ask for the deletion
        User authenticatedUser = this.authService.getCurrentUser( authentication );

        // Only the owner can delete another user
        this.checkRole(spaceId, authenticatedUser.getId(), SpaceRole.OWNER);

        if ( userId.equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("You must transfer ownership of the space before leaving it.");
        }

        // Verify that the user is part of the space
        this.checkMembership(spaceId, userId);

        spaceMemberRepository.deleteBySpaceIdAndUserId(spaceId, userId);
    }
}
