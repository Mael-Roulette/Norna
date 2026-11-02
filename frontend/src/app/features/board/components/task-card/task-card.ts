import { Component, input, output } from '@angular/core';
import { TaskResponse } from '../../../../models/task';
import { provideIcons, NgIcon } from '@ng-icons/core';
import { phosphorTextAlignLeftBold } from '@ng-icons/phosphor-icons/bold';

@Component({
  imports: [NgIcon],
  providers: [provideIcons({phosphorTextAlignLeftBold})],
  selector: 'app-task-card',
  styleUrl: './task-card.css',
  templateUrl: './task-card.html',
})
export class TaskCard {
  task = input.required<TaskResponse>();
  opened = output<TaskResponse>();
}
