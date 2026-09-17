package com.maelrltt.norna.service;

import com.maelrltt.norna.dto.auth.UserResponse;
import com.maelrltt.norna.dto.user.UpdateLastVisitedSpaceRequest;
import com.maelrltt.norna.entity.Space;
import com.maelrltt.norna.entity.User;
import com.maelrltt.norna.exception.ResourceNotFoundException;
import com.maelrltt.norna.repository.SpaceRepository;
import com.maelrltt.norna.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SpaceRepository spaceRepository;
    private final SpaceMemberService spaceMemberService;
    private final AuthService authService;

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

        return new UserResponse(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getLastVisitedSpace().getId(),
                currentUser.getCreatedAt()
        );
    }
}
