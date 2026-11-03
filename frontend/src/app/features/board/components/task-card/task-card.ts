import { Component, computed, input, output } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { lucideCalendar, lucideCheck, lucideX } from '@ng-icons/lucide';
import { phosphorTextAlignLeftBold } from '@ng-icons/phosphor-icons/bold';
import { SpaceMemberWithDetails } from '../../../../models/space';
import { TaskResponse } from '../../../../models/task';
import { fromDateOnly } from '../../../../utils/date';

const MAX_AVATARS = 3;

@Component({
  imports: [NgIcon],
  providers: [
    provideIcons({ phosphorTextAlignLeftBold, lucideCheck, lucideX, lucideCalendar }),
  ],
  selector: 'app-task-card',
  styleUrl: './task-card.css',
  templateUrl: './task-card.html',
})
export class TaskCard {
  task = input.required<TaskResponse>();
  opened = output<TaskResponse>();

  private dueDate = computed(() => fromDateOnly(this.task().dueDate));

  formattedDueDate = computed(() => this.dueDate()?.toLocaleDateString() ?? '');

  isOverdue = computed(() => {
    const date = this.dueDate();
    if (!date || this.task().completed) return false;

    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return date < today;
  });

  visibleUsers = computed(() => this.task().assignedUsers?.slice(0, MAX_AVATARS) ?? []);

  extraUsers = computed(() =>
    Math.max(0, (this.task().assignedUsers?.length ?? 0) - MAX_AVATARS)
  );
}