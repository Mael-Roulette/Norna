import { Component, inject, signal } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { lucideX } from '@ng-icons/lucide';
import { UserService } from '../../../../service/user/user.service';
import { Router } from '@angular/router';

@Component({
  imports: [NgIcon],
  providers: [provideIcons({lucideX})],
  selector: 'app-delete-account-modal',
  styleUrl: './delete-account-modal.css',
  templateUrl: './delete-account-modal.html',
})
export class DeleteAccountModal {
  private userService = inject(UserService);
  private router = inject(Router);

  /* --------------------------------------------- */
  /* ---------- Handle modal visibility ---------- */
  showDeleteUserModal = signal<boolean>(false);
  
  openDeleteAccountModal() {
    this.showDeleteUserModal.set(true);
  }

  closeDeleteAccountModal() {
    this.showDeleteUserModal.set(false);
  }

  /* --------------------------------- */
  /* ---------- Delete user ---------- */
  deleteUser() {
    this.userService.deleteUser().subscribe({
      next: () => {
        this.router.navigate(['/auth/sign-in']);
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
}
