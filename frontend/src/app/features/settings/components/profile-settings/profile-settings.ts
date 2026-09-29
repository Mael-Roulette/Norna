import { Component, ViewChild, inject, signal } from '@angular/core';
import { UserService } from '../../../../service/user/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { form, required, minLength, maxLength, FormField, FormRoot } from '@angular/forms/signals';
import { firstValueFrom } from 'rxjs';
import { DeleteAccountModal } from '../delete-account-modal/delete-account-modal';

@Component({
  imports: [FormRoot, FormField, DeleteAccountModal],
  selector: 'app-profile-settings',
  styleUrl: './profile-settings.css',
  templateUrl: './profile-settings.html',
})
export class ProfileSettings {
  protected userService = inject(UserService);

    /* ------------------------------------- */
  /* ---------- Update username ---------- */
  showUsernameEdit = signal<boolean>( false );

  toggleShowUsernameEdit() {
    if (this.showUsernameEdit()) {
      this.updateUsernameModel.set({ username: this.userService.requireUser().username });
      this.updateUsernameForm().reset();
    }

    this.showUsernameEdit.update( v => !v );
  }

  private readonly INITIAL_MODEL = { username: this.userService.requireUser().username };
  updateUsernameError = signal<string | null>(null);
  updateUsernameModel = signal({ ...this.INITIAL_MODEL })

  updateUsernameForm = form(
    this.updateUsernameModel,
    (schemaPath) => {
      required(schemaPath.username, { message: 'Username is required' });
      minLength(schemaPath.username, 3, { message: 'Username must be at least 3 chars.' });
      maxLength(schemaPath.username, 50, { message: 'Username must be under 50 chars.' });
    },
    {
      submission: {
        action: async(form) => {
          try {
            const updateUsernameRequest = this.updateUsernameModel();

            await firstValueFrom(this.userService.updateUsername(updateUsernameRequest));
            this.userService.refresh();
            this.showUsernameEdit.set(false);
          } catch ( error: unknown ) {
            if (error instanceof HttpErrorResponse) {
              switch (error.status) {
                case 400:
                  this.updateUsernameError.set(
                    error.error?.message ?? 'Please check the information provided.',
                  );
                  break;
                case 500:
                  this.updateUsernameError.set(
                    'Something went wrong on the server. Please try again later.',
                  );
                  break;
                case 0:
                  this.updateUsernameError.set(
                    'Unable to connect to the server. Please check your connection.',
                  );
                  break;
                default:
                  this.updateUsernameError.set(
                    error.error?.message ?? 'An error occurred. Please try again.',
                  );
              }
              return;
            }
            this.updateUsernameError.set('An unexpected error occurred. Please try again.');
          }finally {
            form().reset({...this.INITIAL_MODEL});
          }
        }
      }
    }
  )

  /* --------------------------------------- */
  /* ---------- Delete user modal ---------- */
  @ViewChild(DeleteAccountModal)
  deleteAccountModal!: DeleteAccountModal;

  openDeleteAccountModal() {
    this.deleteAccountModal.openDeleteAccountModal();
  }
}
