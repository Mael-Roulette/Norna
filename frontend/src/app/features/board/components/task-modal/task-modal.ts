import { DatePipe } from '@angular/common';
import { afterNextRender, Component, ElementRef, inject, input, output, signal, viewChild } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { fluentDelete } from '@ng-icons/fluent-ui';
import { lucideCalendar, lucideEdit3, lucideUsers, lucideX } from '@ng-icons/lucide';
import { TaskResponse } from '../../../../models/task';
import { TaskService } from '../../../../service/task/task.service';
import { DatePicker } from '../../../../components/global/date-picker/date-picker';
import { UserSelection } from '../../../../components/global/user-selection/user-selection';
import { SpaceMemberWithDetails } from '../../../../models/space';

@Component({
  imports: [DatePipe, NgIcon, DatePicker, UserSelection],
  providers: [provideIcons({ lucideX, lucideUsers, lucideCalendar, lucideEdit3, fluentDelete })],
  selector: 'app-task-modal',
  templateUrl: './task-modal.html',
})
export class TaskModal {
  private taskService = inject(TaskService);
  private dialog = viewChild.required<ElementRef<HTMLDialogElement>>('dialog');

  readonly task = input.required<TaskResponse>();
  readonly deleted = output<void>();
  readonly closed = output<void>();

  constructor() {
    afterNextRender(() => this.dialog().nativeElement.showModal());
  }

  close() {
    this.dialog().nativeElement.close();
  }

  protected onDialogClosed() {
    this.closed.emit();
  }

  protected deleteTask() {
    this.taskService.deleteTask(this.task().id).subscribe(() => {
      this.deleted.emit();
      this.close();
    });
  }

  /* ---------------------------------- */
  /* ---------- Edition mode ---------- */
  editionMode = signal(false);
  readonly spaceMembers = input.required<SpaceMemberWithDetails[]>();

  toggleEditionMode() {
    this.editionMode.update(v => !v);
  }
}