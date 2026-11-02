import { Component, effect, inject } from '@angular/core';
import { Router } from '@angular/router';
import { SpaceService } from '../../../service/space/space.service';
import { UserService } from '../../../service/user/user.service';

@Component({
  selector: 'app-dashboard-redirect',
  template: '',
})
export class DashboardRedirect {
  private router = inject(Router);
  private spaceService = inject(SpaceService);
  private userService = inject(UserService);

  constructor() {
    effect(() => {
      if (!this.userService.isReady() || !this.spaceService.isReady()) return;

      const spaces = this.spaceService.spaces();
      const lastVisitedSpaceId = this.userService.user()?.lastVisitedSpace;
      const space = spaces.find((s) => s.spaceId === lastVisitedSpaceId) ?? spaces[0];

      if (space) {
        this.router.navigate(['/space', space.spaceId, 'dashboard']);
      } else {
        this.router.navigate(['/auth/sign-in']);
      }
    });
  }
}