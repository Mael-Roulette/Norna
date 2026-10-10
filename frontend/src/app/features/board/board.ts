import { Component, ViewChild, inject, input } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { lucidePlus } from '@ng-icons/lucide';
import { tablerDotsVertical } from '@ng-icons/tabler-icons';
import { BoardService } from '../../service/board/board.service';
import { ColumnService } from '../../service/column/column.service';
import { CreateColumn } from './components/create-column/create-column';
import { rxResource } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-board',
  imports: [NgIcon, CreateColumn],
  providers: [provideIcons({lucidePlus, tablerDotsVertical})],
  templateUrl: './board.html',
  styleUrl: './board.css',
})
export class Board {
  private boardService = inject(BoardService);
  private columnService = inject(ColumnService);

  boardId = input.required<string>();

  protected currentBoard = () =>
    this.boardService.boards().find(
      board => board.id === this.boardId()
    );

  protected columns = rxResource({
    params: () => ({ boardId: this.boardId() }),
    stream: ({ params }) => this.columnService.getBoardColumns(params.boardId),
  });
  /* ----------------------------------------------- */
  /* ---------- Handle add board modal ---------- */
  @ViewChild(CreateColumn)
  createColumnModal!: CreateColumn;

  openCreateColumnModal() {
    this.createColumnModal.openCreateColumnModal();
  }
}