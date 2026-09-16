import { Component, signal } from '@angular/core';
import { lucideChevronUp, lucideChevronDown, lucideUserPlus, lucideHelpCircle } from '@ng-icons/lucide';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { remixSearch2Line } from '@ng-icons/remixicon';
import { ButtonPrimary } from '../../global/button-primary/button-primary';
import { RouterLink } from '@angular/router';

@Component({
  imports: [NgIcon, ButtonPrimary, RouterLink],
  providers: [
    provideIcons({ lucideChevronUp, lucideChevronDown, remixSearch2Line, lucideUserPlus, lucideHelpCircle }),
  ],
  selector: 'app-header',
  styleUrl: './header.css',
  templateUrl: './header.html',
})
export class Header {
  protected readonly showSpacesMenu = signal(false);

  toggleShowSpacesMenu() {
    this.showSpacesMenu.update((v) => !v);
  }
}
