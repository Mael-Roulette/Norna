import { Component, output } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { monoEditAlt } from '@ng-icons/mono-icons';
import { fluentDelete } from '@ng-icons/fluent-ui';

@Component({
  imports: [NgIcon],
  providers: [provideIcons({monoEditAlt, fluentDelete})],
  selector: 'app-column-menu',
  templateUrl: './column-menu.html',
})
export class ColumnMenu {
  delete = output<void>();

  onDelete() {
    this.delete.emit();
  }
}
