package com.maelrltt.norna.service;

import com.maelrltt.norna.dto.auth.UserResponse;
import com.maelrltt.norna.dto.space.CreateSpaceRequest;
import com.maelrltt.norna.dto.space.SpaceResponse;
import com.maelrltt.norna.dto.space.SpaceResponseWithDetails;
import com.maelrltt.norna.dto.spaceMember.SpaceMemberDetailsResponse;
import com.maelrltt.norna.dto.spaceMember.SpaceMemberResponse;
import com.maelrltt.norna.entity.Space;
import com.maelrltt.norna.entity.SpaceMember;
import com.maelrltt.norna.entity.SpaceRole;
import org.springframework.security.core.userdetails.User;
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
            CreateSpaceRequest createSpaceRequest
    ) {

        User user = (User) authentication.getPrincipal();

        com.maelrltt.norna.entity.User currentUser = authService.getUserByUsername(Objects.requireNonNull(user).getUsername());

        final Space newSpace = Space.builder()
                .name(createSpaceRequest.name())
                .build();

        spaceRepository.save(newSpace);

        SpaceMember owner = spaceMemberService.addMember(newSpace, currentUser, SpaceRole.OWNER);

        return new SpaceResponseWithDetails(
                newSpace.getId(),
                newSpace.getName(),
                List.of(
                        new SpaceMemberDetailsResponse(
                                owner.getUser().getId(),
                                owner.getRole().name(),
                                owner.getUser().getEmail(),
                                owner.getRole()
                        )
                )
        );
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
        User user = (User) authentication.getPrincipal();
        com.maelrltt.norna.entity.User currentUser = authService.getUserByUsername(Objects.requireNonNull(user).getUsername());

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
