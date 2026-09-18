import { Component, HostListener, computed, inject, signal } from '@angular/core';
import { lucideLogOut } from '@ng-icons/lucide';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { UserService } from '../../../../service/user/user.service';
import { AuthService } from '../../../../service/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  imports: [NgIcon],
  providers: [provideIcons({ lucideLogOut })],
  selector: 'app-user-menu',
  styleUrl: './user-menu.css',
  templateUrl: './user-menu.html',
})
export class UserMenu {
  protected userService = inject(UserService);
  private authService = inject(AuthService);
  private router = inject(Router);

  /* ------------------------------------------------- */
  /* ---------- Handle user menu visibility ---------- */
  protected showUserMenu = signal(false);
  toggleShowUserMenu() {
    this.showUserMenu.update((v) => !v);
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;

    if (!target.closest('.user-menu-container')) {
      this.showUserMenu.set(false);
    }
  }

  /* ----------------------------------- */
  /* ---------- handle logout ---------- */
  logout() {
    this.authService.logout().subscribe({
      next: () => {
        this.userService.refresh();
        this.router.navigateByUrl('/auth/sign-in');
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
}
