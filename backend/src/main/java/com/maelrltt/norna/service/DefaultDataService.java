package com.maelrltt.norna.service;

import com.maelrltt.norna.entity.Board;
import com.maelrltt.norna.entity.Space;
import com.maelrltt.norna.entity.User;
import com.maelrltt.norna.repository.BoardRepository;
import com.maelrltt.norna.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultDataService {
    private final SpaceRepository spaceRepository;
    private final BoardRepository boardRepository;
    private final SpaceMemberService spaceMemberService;

    public void createDefaultData(User user) {
        final Space newSpace = Space.builder()
                .name("Default space")
                .build();
        spaceRepository.save(newSpace);

        spaceMemberService.createOwner(newSpace, user);

        final Board firstBoard = Board.builder()
                .name("Website")
                .description("A great website")
                .space(newSpace)
                .build();
        boardRepository.save(firstBoard);

        final Board secondBoard = Board.builder()
                .name("Mobile app")
                .description("Android and IOS mobile app")
                .space(newSpace)
                .build();
        boardRepository.save(secondBoard);

        user.setLastVisitedSpace(newSpace);
    }
}
