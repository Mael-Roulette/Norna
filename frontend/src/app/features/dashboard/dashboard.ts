import { Component, inject } from '@angular/core';
import { UserService } from '../../service/user/user.service';
import { AuthService } from '../../service/auth/auth.service';
import { Router } from '@angular/router';
import { SpaceService } from '../../service/space/space.service';

@Component({
  imports: [],
  selector: 'app-dashboard',
  styleUrl: './dashboard.css',
  templateUrl: './dashboard.html',
})
export class Dashboard {
  protected userService = inject(UserService);
  protected authService = inject(AuthService);
  protected spaceService = inject(SpaceService);
  private router = inject(Router);

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/sign-in']);
      },
    });
  }
}
