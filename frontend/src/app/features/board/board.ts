import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
import { map } from 'rxjs';
import { BoardService } from '../../service/board/board.service';
import { lucidePlus } from '@ng-icons/lucide';
import { tablerDotsVertical } from '@ng-icons/tabler-icons';
import { NgIcon, provideIcons } from '@ng-icons/core';

@Component({
  selector: 'app-board',
  imports: [NgIcon],
  providers: [provideIcons({lucidePlus, tablerDotsVertical})],
  templateUrl: './board.html',
  styleUrl: './board.css',
})
export class Board {
  private route = inject(ActivatedRoute);
  private boardService = inject(BoardService);

  private boardId = toSignal(
    this.route.paramMap.pipe(
      map(params => params.get('boardId'))
    )
  );

  protected currentBoard = () =>
    this.boardService.boards().find(
      board => board.id === this.boardId()
    );
}