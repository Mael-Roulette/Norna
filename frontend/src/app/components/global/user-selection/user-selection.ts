import {Combobox, ComboboxPopup, ComboboxWidget} from '@angular/aria/combobox';
import {Listbox, Option} from '@angular/aria/listbox';
import {OverlayModule} from '@angular/cdk/overlay';
import {afterRenderEffect, Component, computed, signal, viewChild, effect, input, model} from '@angular/core';
import { provideIcons, NgIcon } from '@ng-icons/core';
import { lucideChevronDown, lucideCheck } from '@ng-icons/lucide';
import { SpaceMemberWithDetails } from '../../../models/space';
import { FormValueControl } from '@angular/forms/signals';

@Component({
  imports: [Combobox, ComboboxPopup, ComboboxWidget, Listbox, Option, OverlayModule, NgIcon],
  providers: [provideIcons({lucideChevronDown, lucideCheck})],
  selector: 'app-user-selection',
  styleUrl: './user-selection.css',
  templateUrl: './user-selection.html',
})
export class UserSelection implements FormValueControl<string[]> {
  readonly users = input.required<SpaceMemberWithDetails[]>();

   /** The combobox listbox popup. */
  readonly listbox = viewChild(Listbox);

  /** The options available in the listbox. */
  readonly value = model<string[]>([]);
  readonly selectedValues = signal<string[]>([]);

  /** The selected users, shown as avatars in the combobox. */
  readonly displayIcons = computed(() => {
    const values = this.selectedValues();
    return this.users().filter((user) => values.includes(user.userId));
  });

  readonly visibleIcons = computed(() => this.displayIcons().slice(0, 3));
  readonly extraCount = computed(() => Math.max(0, this.displayIcons().length - 3));

  /** Whether the popup is expanded. */
  readonly popupExpanded = signal(false);

  constructor() {
    // Scrolls to the active item when the active option changes.
    afterRenderEffect(() => {
      this.listbox()?.scrollActiveItemIntoView();
    });
  }
}
