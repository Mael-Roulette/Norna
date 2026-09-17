import { Component, inject, signal } from '@angular/core';
import { FormField, FormRoot, form, maxLength, minLength, required } from '@angular/forms/signals';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { lucideX } from '@ng-icons/lucide';
import { firstValueFrom } from 'rxjs';
import { SpaceService } from '../../../service/space/space.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  imports: [NgIcon, FormField, FormRoot],
  providers: [provideIcons({ lucideX })],
  selector: 'app-create-space',
  styleUrl: './create-space.css',
  templateUrl: './create-space.html',
})
export class CreateSpace {
  protected spaceService = inject(SpaceService);

  /* -------------------------------------------- */
  /* ---------- Handle the space modal creation ---------- */
  protected readonly showCreateSpaceModal = signal(false);
  openCreateSpaceModal() {
    this.showCreateSpaceModal.set(true);
  }

  closeCreateSpaceModal() {
    this.showCreateSpaceModal.set(false);
  }

  /* --------------------------------------- */
  /* ---------- Create space form ---------- */
  createSpaceError = signal<string | null>(null);
  createSpaceModel = signal({
    name: '',
  });

  createSpaceForm = form(
    this.createSpaceModel,
    (schemaPath) => {
      // Name verification
      required(schemaPath.name, { message: 'Name is required' });
      minLength(schemaPath.name, 4, { message: 'Space name must be at least 4 chars.' });
      maxLength(schemaPath.name, 20, { message: 'Space name must be under 20 chars.' });
    },
    {
      submission: {
        action: async () => {
          try {
            const spaceRequest = this.createSpaceModel();

            await firstValueFrom(this.spaceService.createSpace(spaceRequest));

            this.closeCreateSpaceModal();
          } catch (error: unknown) {
            if (error instanceof HttpErrorResponse) {
              switch (error.status) {
                case 400:
                  this.createSpaceError.set(
                    error.error?.message ?? 'Please check the information provided.',
                  );
                  break;
                case 500:
                  this.createSpaceError.set(
                    'Something went wrong on the server. Please try again later.',
                  );
                  break;
                case 0:
                  this.createSpaceError.set(
                    'Unable to connect to the server. Please check your connection.',
                  );
                  break;
                default:
                  this.createSpaceError.set(
                    error.error?.message ?? 'An error occurred. Please try again.',
                  );
              }
              return;
            }
            this.createSpaceError.set('An unexpected error occurred. Please try again.');
          }
        },
      },
    },
  );
}
