import { Component, inject, input, output, signal } from '@angular/core';
import { FormField, FormRoot, form, maxLength, minLength, required } from '@angular/forms/signals';
import { provideIcons, NgIcon } from '@ng-icons/core';
import { lucideChevronDown, lucideX } from '@ng-icons/lucide';
import { TaskService } from '../../../../service/task/task.service';
import { firstValueFrom } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { DatePicker } from '../../../../components/global/date-picker/date-picker';
import { TaskFormModel, TaskRequest, TaskResponse } from '../../../../models/task';
import { toDateOnly } from '../../../../utils/date';
import { SpaceMemberWithDetails } from '../../../../models/space';
import { UserSelection } from '../../../../components/global/user-selection/user-selection';

@Component({
  imports: [NgIcon, FormField, FormRoot, DatePicker, UserSelection],
  providers: [provideIcons({ lucideX, lucideChevronDown })],
  selector: 'app-create-task',
  templateUrl: './create-task.html',
})
export class CreateTask {
  private taskService = inject(TaskService);
  readonly spaceMembers = input.required<SpaceMemberWithDetails[]>();

  columnId = input<string | null>(null);
  created = output<TaskResponse>();

  protected readonly showCreateTaskModal = signal(false);
  openCreateTask() {
    this.showCreateTaskModal.set(true);
  }

  closeCreateTask() {
    this.showCreateTaskModal.set(false);
  }

  /* --------------------------------------- */
  /* ---------- Create task form ---------- */
  private readonly INITIAL_MODEL: TaskFormModel = {
    title: '',
    description: '',
    dueDate: null,
    assignedUserIds: []
  }

  createTaskError = signal<string | null>(null);
  createTaskModel = signal<TaskFormModel>({...this.INITIAL_MODEL});

  createTaskForm = form(
    this.createTaskModel,
    (schemaPath) => {
      // Title verification
      required(schemaPath.title, { message: 'Title is required' });
      minLength(schemaPath.title, 4, { message: 'Task title must be at least 4 chars.' });
      maxLength(schemaPath.title, 30, { message: 'Task title must be under 30 chars.' });

      // Description verification
      maxLength(schemaPath.description, 500, { message: 'Task description must be under 500 chars.' });
    },
    {
      submission: {
        action: async (form) => {
          try {
            const columnId = this.columnId();
            if (!columnId) {
              this.createTaskError.set('Cannot determine the current column.');
              return;
            }

            const model = this.createTaskModel();

            const taskRequest: TaskRequest = {
              ...model,
              dueDate: toDateOnly(model.dueDate),
            };


            const created = await firstValueFrom(this.taskService.createTask(columnId, taskRequest));

            this.created.emit(created);
            this.closeCreateTask();
            form().reset({...this.INITIAL_MODEL});
          } catch (error: unknown) {
            if (error instanceof HttpErrorResponse) {
              switch (error.status) {
                case 400:
                  this.createTaskError.set(
                    error.error?.message ?? 'Please check the information provided.',
                  );
                  break;
                case 401:
                  this.createTaskError.set('Your session has expired. Please log in again.');
                  break;
                case 403:
                  this.createTaskError.set(error.error?.message ?? 'You do not have permission to create tasks here.');
                  break;
                case 500:
                  this.createTaskError.set(
                    'Something went wrong on the server. Please try again later.',
                  );
                  break;
                case 0:
                  this.createTaskError.set(
                    'Unable to connect to the server. Please check your connection.',
                  );
                  break;
                default:
                  this.createTaskError.set(
                    error.error?.message ?? 'An error occurred. Please try again.',
                  );
              }
              return;
            }
            this.createTaskError.set('An unexpected error occurred. Please try again.');
          }
        }
      }
    }
  )
}
