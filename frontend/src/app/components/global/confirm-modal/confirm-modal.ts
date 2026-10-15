import { Component, input, output } from '@angular/core';

@Component({
  imports: [],
  selector: 'app-confirm-modal',
  styleUrl: './confirm-modal.css',
  templateUrl: './confirm-modal.html',
})
export class ConfirmModal {
  show = input<boolean>(false);

  title = input.required<string>();
  description = input.required<string>();
  confirmText = input.required<string>();
  cancelText = input<string>('Cancel');

  confirmed = output<void>();
  cancelled = output<void>();
}