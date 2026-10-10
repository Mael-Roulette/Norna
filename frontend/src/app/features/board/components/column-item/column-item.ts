import { Component, input } from '@angular/core';
import { BoardColumnResponse } from '../../../../models/column';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { tablerGripVertical } from '@ng-icons/tabler-icons';

@Component({
  imports: [NgIcon],
  providers: [provideIcons({tablerGripVertical})],
  selector: 'app-column-item',
  templateUrl: './column-item.html',
})
export class ColumnItem {
  column = input<BoardColumnResponse | null>(null);
}
