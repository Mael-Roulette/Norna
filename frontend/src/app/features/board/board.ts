import { Component, inject, input, linkedSignal, viewChild } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray } from '@angular/cdk/drag-drop';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { lucidePlus } from '@ng-icons/lucide';
import { tablerDotsVertical } from '@ng-icons/tabler-icons';
import { BoardService } from '../../service/board/board.service';
import { ColumnService } from '../../service/column/column.service';
import { CreateColumn } from './components/create-column/create-column';
import { ColumnItem } from './components/column-item/column-item';

@Component({
  selector: 'app-board',
  imports: [NgIcon, CreateColumn, ColumnItem, CdkDropList, CdkDrag],
  providers: [provideIcons({ lucidePlus, tablerDotsVertical })],
  templateUrl: './board.html',
  styleUrl: './board.css',
})
export class Board {
  private boardService = inject(BoardService);
  private columnService = inject(ColumnService);

  boardId = input.required<string>();

  protected currentBoard = () =>
    this.boardService.boards().find(board => board.id === this.boardId());

  protected columns = rxResource({
    params: () => ({ boardId: this.boardId() }),
    stream: ({ params }) => this.columnService.getBoardColumns(params.boardId),
  });

  protected orderedColumns = linkedSignal(() => this.columns.value() ?? []);

  /* ---------- Create column modal ---------- */
  private createColumnModal = viewChild.required(CreateColumn);

  openCreateColumnModal() {
    this.createColumnModal().openCreateColumnModal();
  }

  /* ---------- Drag and drop ---------- */
  drop(event: CdkDragDrop<unknown>) {
    const { previousIndex, currentIndex } = event;
    if (previousIndex === currentIndex) return;

    const previous = this.orderedColumns();
    const moved = previous[previousIndex];

    const next = [...previous];
    moveItemInArray(next, previousIndex, currentIndex);
    this.orderedColumns.set(next);

    this.columnService
      .updateColumnPosition(this.boardId(), moved.id, { position: currentIndex })
      .subscribe({
        error: () => {
          this.orderedColumns.set(previous);
        },
      });
  }
}