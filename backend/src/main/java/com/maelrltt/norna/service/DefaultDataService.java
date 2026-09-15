package com.maelrltt.norna.service;

import com.maelrltt.norna.entity.Space;
import com.maelrltt.norna.entity.SpaceMember;
import com.maelrltt.norna.entity.SpaceRole;
import com.maelrltt.norna.entity.User;
import com.maelrltt.norna.repository.SpaceMemberRepository;
import com.maelrltt.norna.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultDataService {
    private final SpaceRepository spaceRepository;
    private final SpaceMemberService spaceMemberService;

    public void createDefaultData(User user) {
        final Space newSpace = Space.builder()
                .name("Default space")
                .build();
        spaceRepository.save(newSpace);

        spaceMemberService.addMember(newSpace, user, SpaceRole.OWNER);

        user.setLastVisitedSpace(newSpace);
    }
}
