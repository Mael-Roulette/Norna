import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, input, signal } from '@angular/core';
import { FormField, FormRoot, form, maxLength, minLength, required } from '@angular/forms/signals';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { lucideX } from '@ng-icons/lucide';
import { firstValueFrom } from 'rxjs';
import { BoardService } from '../../../service/board/board.service';

@Component({
  imports: [NgIcon, FormField, FormRoot],
  providers: [provideIcons({ lucideX })],
  selector: 'app-create-board',
  styleUrl: './create-board.css',
  templateUrl: './create-board.html',
})
export class CreateBoard {
  private boardService = inject(BoardService);

  spaceId = input<string | null>(null);

  /* -------------------------------------------- */
  /* ---------- Handle the board modal ---------- */
  protected readonly showAddBoardModal = signal(false);
  openAddBoardModal() {
    this.showAddBoardModal.set(true);
  }

  closeAddBoardModal() {
    this.showAddBoardModal.set(false);
  }

  /* --------------------------------------- */
  /* ---------- Add board form ---------- */
  addBoardError = signal<string | null>(null);
  addBoardModel = signal({
    name: '',
    description: ''
  });

  addBoardForm = form(
    this.addBoardModel,
    (schemaPath) => {
      // Name verification
      required(schemaPath.name, { message: 'Name is required' });
      minLength(schemaPath.name, 4, { message: 'Board name must be at least 4 chars.' });
      maxLength(schemaPath.name, 20, { message: 'Board name must be under 20 chars.' });
    },
    {
      submission: {
        action: async () => {
          try {
            const spaceId = this.spaceId();
            if (!spaceId) {
              this.addBoardError.set('Cannot determine the current space.');
              return;
            }

            const boardRequest = this.addBoardModel();

            await firstValueFrom(this.boardService.createBoard(spaceId, boardRequest));
            this.boardService.refresh();

            this.closeAddBoardModal();
          } catch (error: unknown) {
            if (error instanceof HttpErrorResponse) {
              switch (error.status) {
                case 400:
                  this.addBoardError.set(
                    error.error?.message ?? 'Please check the information provided.',
                  );
                  break;
                case 500:
                  this.addBoardError.set(
                    'Something went wrong on the server. Please try again later.',
                  );
                  break;
                case 0:
                  this.addBoardError.set(
                    'Unable to connect to the server. Please check your connection.',
                  );
                  break;
                default:
                  this.addBoardError.set(
                    error.error?.message ?? 'An error occurred. Please try again.',
                  );
              }
              return;
            }
            this.addBoardError.set('An unexpected error occurred. Please try again.');
          }
        },
      },
    },
  );
}
