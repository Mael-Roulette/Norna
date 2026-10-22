import { Component, input, output } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { tablerAlertTriangle } from '@ng-icons/tabler-icons';

@Component({
  imports: [NgIcon],
  providers: [provideIcons({ tablerAlertTriangle })],
  selector: 'app-confirm-modal',
  styleUrl: './confirm-modal.css',
  templateUrl: './confirm-modal.html',
})
export class ConfirmModal {
  show = input<boolean>(false);
  variant = input<'default' | 'danger'>('default');

  title = input.required<string>();
  description = input.required<string>();
  confirmText = input.required<string>();
  cancelText = input<string>('Cancel');

  confirmed = output<void>();
  cancelled = output<void>();
}