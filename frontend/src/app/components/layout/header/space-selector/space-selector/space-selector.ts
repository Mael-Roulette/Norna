import { Component, ViewChild, computed, inject, signal } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { lucideChevronDown, lucideChevronUp, lucidePlus } from '@ng-icons/lucide';
import { UserService } from '../../../../../service/user/user.service';
import { SpaceService } from '../../../../../service/space/space.service';
import { CreateSpace } from '../../../../space/create-space/create-space';
import { Router } from '@angular/router';

@Component({
  imports: [NgIcon, CreateSpace],
  providers: [
    provideIcons({
      lucideChevronUp,
      lucideChevronDown,
      lucidePlus,
    }),
  ],
  selector: 'app-space-selector',
  styleUrl: './space-selector.css',
  templateUrl: './space-selector.html',
})
export class SpaceSelector {
  protected userService = inject(UserService);
  protected spaceService = inject(SpaceService);
  private router = inject(Router);

  lastVisitedSpace = computed(() => {
    const lastVisitedSpaceId = this.userService.user()?.lastVisitedSpace;

    return this.spaceService.spaces().find((space) => space.spaceId === lastVisitedSpaceId);
  });

  actualSpace = computed(() => {
    return this.lastVisitedSpace() ?? this.spaceService.spaces()[0];
  });

  /* -------------------------------------------- */
  /* ---------- Handle the spaces menu ---------- */
  protected readonly showSpacesMenu = signal(false);
  toggleShowSpacesMenu() {
    this.showSpacesMenu.update((v) => !v);
  }

  switchSpace(spaceId: string) {
    this.userService.updateLastVisitedSpace({ spaceId }).subscribe({
      next: () => {
        this.userService.refresh();
        this.router.navigateByUrl(`/dashboard/${spaceId}`);
      },
      error: (error) => {
        console.error(error);
      },
    });
  }

  /* ----------------------------------------------- */
  /* ---------- Handle create space modal ---------- */
  @ViewChild(CreateSpace)
  createSpaceModal!: CreateSpace;

  openCreateSpaceModal() {
    this.createSpaceModal.openCreateSpaceModal();
  }
}
