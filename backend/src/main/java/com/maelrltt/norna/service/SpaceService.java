package com.maelrltt.norna.service;

import com.maelrltt.norna.dto.space.SpaceRequest;
import com.maelrltt.norna.dto.space.SpaceResponse;
import com.maelrltt.norna.dto.space.SpaceResponseWithDetails;
import com.maelrltt.norna.dto.spaceMember.SpaceMemberDetailsResponse;
import com.maelrltt.norna.dto.spaceMember.SpaceMemberResponse;
import com.maelrltt.norna.entity.Space;
import com.maelrltt.norna.entity.SpaceMember;
import com.maelrltt.norna.entity.SpaceRole;
import com.maelrltt.norna.entity.User;
import com.maelrltt.norna.repository.SpaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpaceService {
    private final SpaceRepository spaceRepository;
    private final SpaceMemberService spaceMemberService;
    private final AuthService authService;

    @Transactional
    public SpaceResponseWithDetails createSpace(
            Authentication authentication,
            SpaceRequest spaceRequest
    ) {
        User currentUser = authService.getCurrentUser(authentication);

        final Space newSpace = Space.builder()
                .name(spaceRequest.name())
                .build();

        spaceRepository.save(newSpace);

        SpaceMember owner = spaceMemberService.createOwner(newSpace, currentUser);

        return new SpaceResponseWithDetails(
                newSpace.getId(),
                newSpace.getName(),
                List.of(
                        new SpaceMemberDetailsResponse(
                                owner.getUser().getId(),
                                owner.getUser().getUsername(),
                                owner.getUser().getEmail(),
                                owner.getRole()
                        )
                )
        );
    }

    @Transactional
    public SpaceResponseWithDetails updateSpace(Authentication authentication, UUID spaceId, SpaceRequest spaceRequest) {
        User currentUser = authService.getCurrentUser(authentication);

        // Only the owner can update the space
        spaceMemberService.checkRole(spaceId, currentUser.getId(), SpaceRole.OWNER);

        Space space = spaceRepository.getSpaceById(spaceId);

        space.setName(spaceRequest.name());

        return new SpaceResponseWithDetails(
                space.getId(),
                space.getName(),
                space.getMembers().stream().map(
                        spaceMember -> new SpaceMemberDetailsResponse(
                                spaceMember.getUser().getId(),
                                spaceMember.getUser().getUsername(),
                                spaceMember.getUser().getEmail(),
                                spaceMember.getRole()
                        )
                ).toList()
        );
    }

    public void deleteSpace(Authentication authentication, UUID spaceId) {
        User currentUser = authService.getCurrentUser(authentication);

        // Only the owner can delete the space
        spaceMemberService.checkRole(spaceId, currentUser.getId(), SpaceRole.OWNER);

        spaceRepository.deleteById(spaceId);
    }

    public List<SpaceResponse> getSpaces(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        com.maelrltt.norna.entity.User currentUser = authService.getUserByUsername(Objects.requireNonNull(user).getUsername());

        List<SpaceMember> memberships = spaceMemberService.getMembershipsForUser(currentUser.getId());

        return memberships.stream()
                .map(member -> new SpaceResponse(
                        member.getSpace().getId(),
                        member.getSpace().getName(),
                        List.of(
                                new SpaceMemberResponse(
                                        member.getUser().getId(),
                                        member.getRole()
                                )
                        )
                ))
                .toList();
    }

    public SpaceResponseWithDetails getSpaceById(Authentication authentication, UUID spaceId) {
        User currentUser = authService.getCurrentUser(authentication);

        // Check if the user is part of the space
        spaceMemberService.checkMembership(spaceId, currentUser.getId());

        Space space = spaceRepository.getSpaceById(spaceId);

        return new SpaceResponseWithDetails(
                space.getId(),
                space.getName(),
                space.getMembers().stream().map(
                        spaceMember -> new SpaceMemberDetailsResponse(
                                spaceMember.getId(),
                                spaceMember.getUser().getUsername(),
                                spaceMember.getUser().getEmail(),
                                spaceMember.getRole()
                        )
                ).toList()
        );
    }
}
