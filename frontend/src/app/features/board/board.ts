import { CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray } from '@angular/cdk/drag-drop';
import { Component, inject, input, linkedSignal, viewChild } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { lucidePlus } from '@ng-icons/lucide';
import { tablerDotsVertical } from '@ng-icons/tabler-icons';
import { BoardColumnResponse } from '../../models/column';
import { BoardService } from '../../service/board/board.service';
import { ColumnService } from '../../service/column/column.service';
import { ColumnItem } from './components/column-item/column-item';
import { CreateColumn } from './components/create-column/create-column';
import { TaskModal } from './components/task-modal/task-modal';
import { TaskResponse } from '../../models/task';

@Component({
  selector: 'app-board',
  imports: [NgIcon, CreateColumn, ColumnItem, CdkDropList, CdkDrag, TaskModal],
  providers: [provideIcons({ lucidePlus, tablerDotsVertical })],
  templateUrl: './board.html',
  styleUrl: './board.css',
})
export class Board {
onTaskUpdated($event: Event) {
throw new Error('Method not implemented.');
}
onTaskDeleted($event: Event) {
throw new Error('Method not implemented.');
}
  private boardService = inject(BoardService);
  private columnService = inject(ColumnService);

  spaceId = input.required<string>();
  boardId = input.required<string>();

  protected board = rxResource({
    params: () => ({ spaceId: this.spaceId(), boardId: this.boardId() }),
    stream: ({ params }) =>
      this.boardService.getBoardById(params.spaceId, params.boardId),
  });

  protected orderedColumns = linkedSignal(() =>
    [...(this.board.value()?.columns ?? [])].sort((a, b) => a.position - b.position)
  );

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

  /* ----- Rename column ----- */
  onColumnRenamed(updated: BoardColumnResponse): void {
    this.orderedColumns.update(list =>
      list.map(c => (c.id === updated.id ? updated : c))
    );
  }

  /* -------------------------- */
  /* ---------- Task ---------- */
  private taskModal = viewChild.required(TaskModal);

  openTaskModal(task: TaskResponse) {
    this.taskModal().open(task);
  }
}