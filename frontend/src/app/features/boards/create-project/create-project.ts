import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, input, signal } from '@angular/core';
import { FormField, FormRoot, form, required } from '@angular/forms/signals';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { lucideFolder, lucideCheck } from '@ng-icons/lucide';
import { firstValueFrom } from 'rxjs';
import { ProjectService } from '../../../service/project/project.service';

@Component({
  imports: [NgIcon, FormField, FormRoot],
  providers: [provideIcons({ lucideFolder, lucideCheck })],
  selector: 'app-create-project',
  styleUrl: './create-project.css',
  templateUrl: './create-project.html',
})
export class CreateProject {
  private projectService = inject(ProjectService);

  spaceId = input<string | null>(null);

    /* -------------------------------------------- */
  /* ---------- Handle the project creation ---------- */
  protected readonly showCreateProject = signal(false);
  openCreateProject() {
    this.showCreateProject.set(true);
  }

  closeCreateProject() {
    this.showCreateProject.set(false);
  }


  /* --------------------------------------- */
  /* ---------- Create project form ---------- */
  createProjectError = signal<string | null>(null);

  private readonly INITIAL_MODEL = { name: 'New folder', boardsId: [] };
  createProjectModel = signal({ ...this.INITIAL_MODEL });

  createProjectForm = form(
    this.createProjectModel,
    (schemaPath) => {
      // Name verification
      required(schemaPath.name, { message: 'Project name is required' });
    },
    {
      submission: {
        action: async (form) => {
          try {
            const spaceId = this.spaceId();
            if (!spaceId) {
              this.createProjectError.set('Cannot determine the current space.');
              return;
            }

            const projectRequest = this.createProjectModel();

            await firstValueFrom(this.projectService.createProject(spaceId, projectRequest));
            this.projectService.refresh();

            this.closeCreateProject();
          } catch (error: unknown) {
            if (error instanceof HttpErrorResponse) {
              switch (error.status) {
                case 400:
                  this.createProjectError.set(
                    error.error?.message ?? 'Please check the information provided.',
                  );
                  break;
                case 500:
                  this.createProjectError.set(
                    'Something went wrong on the server. Please try again later.',
                  );
                  break;
                case 0:
                  this.createProjectError.set(
                    'Unable to connect to the server. Please check your connection.',
                  );
                  break;
                default:
                  this.createProjectError.set(
                    error.error?.message ?? 'An error occurred. Please try again.',
                  );
              }
              return;
            }
            this.createProjectError.set('An unexpected error occurred. Please try again.');
          } finally {
            form().reset({ ...this.INITIAL_MODEL });
          }
        },
      },
    },
  );
}