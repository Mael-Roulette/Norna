import { Component, inject, input, output, signal } from '@angular/core';
import { ColumnService } from '../../../../service/column/column.service';
import { FormField, FormRoot, form, maxLength, minLength, required } from '@angular/forms/signals';
import { lucideX } from '@ng-icons/lucide';
import { HttpErrorResponse } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { NgIcon, provideIcons } from '@ng-icons/core';

@Component({
  imports: [FormRoot, FormField,  NgIcon],
  providers: [provideIcons({ lucideX })],
  selector: 'app-create-column',
  templateUrl: './create-column.html',
})
export class CreateColumn {
  private columnService = inject(ColumnService);

  boardId = input<string | null>(null);
  created = output<void>();

  /* -------------------------------------------- */
  /* ---------- Handle the column modal ---------- */
  protected readonly showColumnModal = signal(false);
  openCreateColumnModal() {
    this.showColumnModal.set(true);
  }

  closeCreateColumnModal() {
    this.showColumnModal.set(false);
  }

  /* --------------------------------------- */
  /* ---------- Add board form ---------- */
  private readonly INITIAL_MODEL = { name: '', position: 0 }
  addColumnFormError = signal<string | null>(null);
  addColumnFormModel = signal({...this.INITIAL_MODEL});

  addColumnForm = form(
    this.addColumnFormModel,
    (schemaPath) => {
      // Name verification
      required(schemaPath.name, { message: 'Name is required' });
      minLength(schemaPath.name, 4, { message: 'Board name must be at least 4 chars.' });
      maxLength(schemaPath.name, 20, { message: 'Board name must be under 20 chars.' });
    },
    {
      submission: {
        action: async (form) => {
          try {
            const boardId = this.boardId();
            if (!boardId) {
              this.addColumnFormError.set('Cannot determine the current board.');
              return;
            }

            const columnRequest = this.addColumnFormModel();

            await firstValueFrom(this.columnService.createColumn(boardId, columnRequest));
            this.created.emit()

            form().reset({...this.INITIAL_MODEL});

            this.closeCreateColumnModal();
          } catch (error: unknown) {
            if (error instanceof HttpErrorResponse) {
              switch (error.status) {
                case 400:
                  this.addColumnFormError.set(
                    error.error?.message ?? 'Please check the information provided.',
                  );
                  break;
                case 500:
                  this.addColumnFormError.set(
                    'Something went wrong on the server. Please try again later.',
                  );
                  break;
                case 0:
                  this.addColumnFormError.set(
                    'Unable to connect to the server. Please check your connection.',
                  );
                  break;
                default:
                  this.addColumnFormError.set(
                    error.error?.message ?? 'An error occurred. Please try again.',
                  );
              }
              return;
            }
            this.addColumnFormError.set('An unexpected error occurred. Please try again.');
          }
        },
      },
    },
  );
}
