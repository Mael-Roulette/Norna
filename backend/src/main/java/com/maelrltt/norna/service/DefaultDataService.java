package com.maelrltt.norna.service;

import com.maelrltt.norna.entity.Board;
import com.maelrltt.norna.entity.BoardColumn;
import com.maelrltt.norna.entity.Space;
import com.maelrltt.norna.entity.User;
import com.maelrltt.norna.repository.BoardColumnRepository;
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
    private final BoardColumnRepository boardColumnRepository;

    public void createDefaultData(User user) {
        final Space newSpace = Space.builder()
                .name("Default space")
                .build();
        spaceRepository.save(newSpace);

        spaceMemberService.createOwner(newSpace, user);

        // First board creation
        final Board firstBoard = Board.builder()
                .name("Website")
                .description("A great website")
                .space(newSpace)
                .build();
        boardRepository.save(firstBoard);

        final BoardColumn BoardOneFirstColumn = BoardColumn.builder()
                .board(firstBoard)
                .name("To do")
                .position(0)
                .build();
        boardColumnRepository.save(BoardOneFirstColumn);

        final BoardColumn BoardOneSecondColumn = BoardColumn.builder()
                .board(firstBoard)
                .name("In progress")
                .position(1)
                .build();
        boardColumnRepository.save(BoardOneSecondColumn);

        final BoardColumn BoardOneThirdColumn = BoardColumn.builder()
                .board(firstBoard)
                .name("done")
                .position(2)
                .build();
        boardColumnRepository.save(BoardOneThirdColumn);

        // Second board creation
        final Board secondBoard = Board.builder()
                .name("Mobile app")
                .description("Android and IOS mobile app")
                .space(newSpace)
                .build();
        boardRepository.save(secondBoard);

        final BoardColumn BoardTwoFirstColumn = BoardColumn.builder()
                .board(secondBoard)
                .name("To do")
                .position(0)
                .build();
        boardColumnRepository.save(BoardTwoFirstColumn);

        final BoardColumn BoardTwoSecondColumn = BoardColumn.builder()
                .board(secondBoard)
                .name("In progress")
                .position(1)
                .build();
        boardColumnRepository.save(BoardTwoSecondColumn);

        final BoardColumn BoardTwoThirdColumn = BoardColumn.builder()
                .board(secondBoard)
                .name("done")
                .position(2)
                .build();
        boardColumnRepository.save(BoardTwoThirdColumn);

        user.setLastVisitedSpace(newSpace);
    }
}
