import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { lucideHelpCircle, lucideUserPlus } from '@ng-icons/lucide';
import { remixSearch2Line } from '@ng-icons/remixicon';
import { UserService } from '../../../service/user/user.service';
import { ButtonPrimary } from '../../global/button-primary/button-primary';
import { SpaceSelector } from './space-selector/space-selector/space-selector';

@Component({
  imports: [NgIcon, ButtonPrimary, RouterLink, SpaceSelector],
  providers: [
    provideIcons({
      remixSearch2Line,
      lucideUserPlus,
      lucideHelpCircle,
    }),
  ],
  selector: 'app-header',
  styleUrl: './header.css',
  templateUrl: './header.html',
})
export class Header {
  protected userService = inject(UserService);
}
