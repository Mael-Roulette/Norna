package com.maelrltt.norna.service;

import com.maelrltt.norna.dto.auth.UserResponse;
import com.maelrltt.norna.dto.user.UpdateLastVisitedSpaceRequest;
import com.maelrltt.norna.dto.user.UpdatePreferredThemeRequest;
import com.maelrltt.norna.dto.user.UpdateUsernameRequest;
import com.maelrltt.norna.entity.Space;
import com.maelrltt.norna.entity.SpaceMember;
import com.maelrltt.norna.entity.SpaceRole;
import com.maelrltt.norna.entity.User;
import com.maelrltt.norna.exception.ResourceNotFoundException;
import com.maelrltt.norna.mapper.UserMapper;
import com.maelrltt.norna.repository.SpaceMemberRepository;
import com.maelrltt.norna.repository.SpaceRepository;
import com.maelrltt.norna.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SpaceRepository spaceRepository;
    private final SpaceMemberService spaceMemberService;
    private final AuthService authService;
    private final UserMapper userMapper;
    private final SpaceMemberRepository spaceMemberRepository;

    @Transactional
    public UserResponse updateLastVisitedSpace(
            Authentication authentication,
            UpdateLastVisitedSpaceRequest request
    ) {
        User currentUser = authService.getCurrentUser(authentication);

        Space space = spaceRepository.findById(request.spaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Space not found"));

        spaceMemberService.checkMembership(request.spaceId(), currentUser.getId());

        currentUser.setLastVisitedSpace(space);

        userRepository.save(currentUser);

        return userMapper.toResponse(currentUser);
    }

    @Transactional
    public UserResponse updateUsername(
            Authentication authentication,
            UpdateUsernameRequest request
    ) {
        User currentUser = authService.getCurrentUser(authentication);

        currentUser.setUsername(request.username());
        userRepository.save(currentUser);

        return userMapper.toResponse(currentUser);
    }

    @Transactional
    public UserResponse updatePreferredTheme(
            Authentication authentication,
            UpdatePreferredThemeRequest request
    ) {
        User currentUser = authService.getCurrentUser(authentication);

        currentUser.setPreferredTheme(request.newPreferredTheme());
        userRepository.save(currentUser);

        return userMapper.toResponse(currentUser);
    }

    @Transactional
    public void deleteUser(Authentication authentication) {
        User currentUser = authService.getCurrentUser(authentication);

        for (SpaceMember membership : spaceMemberRepository.findByUserId(currentUser.getId())) {
            if (membership.getRole() != SpaceRole.OWNER) {
                continue;
            }

            Space space = membership.getSpace();

            // If the space has another owner we do nothing
            if (spaceMemberRepository.existsBySpaceIdAndRoleAndUserIdNot(
                    space.getId(), SpaceRole.OWNER, currentUser.getId())) {
                continue;
            }

            // The oldest member become the owner or the viewer if no member
            Optional<SpaceMember> successor = spaceMemberRepository
                    .findFirstBySpaceIdAndRoleAndUserIdNotOrderByJoinedAtAsc(
                            space.getId(), SpaceRole.MEMBER, currentUser.getId())
                    .or(() -> spaceMemberRepository
                            .findFirstBySpaceIdAndRoleAndUserIdNotOrderByJoinedAtAsc(
                                    space.getId(), SpaceRole.VIEWER, currentUser.getId()));

            if (successor.isPresent()) {
                successor.get().setRole(SpaceRole.OWNER);
            } else {
                // If there is nobody in the space, the space is deleted
                spaceRepository.delete(space);
            }
        }

        userRepository.delete(currentUser);
    }
}
