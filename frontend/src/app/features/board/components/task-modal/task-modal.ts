import { DatePipe } from '@angular/common';
import { Component, ElementRef, signal, viewChild } from '@angular/core';
import { TaskResponse } from '../../../../models/task';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { lucideX, lucideUsers, lucideCalendar } from '@ng-icons/lucide';

@Component({
  imports: [DatePipe, NgIcon],
  providers: [provideIcons({lucideX, lucideUsers, lucideCalendar})],
  selector: 'app-task-modal',
  styleUrl: './task-modal.css',
  templateUrl: './task-modal.html',
})
export class TaskModal {
  private dialog = viewChild.required<ElementRef<HTMLDialogElement>>('dialog');
  protected task = signal<TaskResponse | null>(null);

  open(task: TaskResponse) {
    this.task.set(task);
    this.dialog().nativeElement.showModal();
  }

  close() {
    this.dialog().nativeElement.close();
  }
}