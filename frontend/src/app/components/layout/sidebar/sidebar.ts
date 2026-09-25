import { Component, Signal, ViewChild, computed, inject, input, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgIcon, provideIcons } from '@ng-icons/core';
import {
  lucideChevronDown,
  lucideChevronUp,
  lucideFolder,
  lucideFolderOpen,
  lucideHome,
  lucidePlus,
  lucideSettings,
  lucideUsersRound
} from '@ng-icons/lucide';
import { phosphorSuitcaseBold } from '@ng-icons/phosphor-icons/bold';
import { CreateBoard } from '../../../features/board/components/create-board/create-board';
import { BoardResponse } from '../../../models/board';
import { BoardService } from '../../../service/board/board.service';
import { ProjectService } from '../../../service/project/project.service';
import { SpaceService } from '../../../service/space/space.service';

@Component({
  imports: [RouterLink, NgIcon, CreateBoard],
  providers: [
    provideIcons({
      lucideHome,
      phosphorSuitcaseBold,
      lucideFolderOpen,
      lucideFolder,
      lucideChevronUp,
      lucideChevronDown,
      lucideUsersRound,
      lucideSettings,
      lucidePlus
    }),
  ],
  selector: 'app-sidebar',
  styleUrl: './sidebar.css',
  templateUrl: './sidebar.html',
})
export class Sidebar {
  protected spaceService = inject(SpaceService);
  protected boardService = inject(BoardService);
  protected projectService = inject(ProjectService);

  spaceId = input<string | null>(null);

  // Get all boards in all projects
  boardsInProject: Signal<BoardResponse[]> = computed(() =>
    this.projectService
      .projects()
      .flatMap(project => project.boards)
  );

  // Get all boards that are not in a project
  boardsOutsideProject: Signal<BoardResponse[]> = computed(() =>
    this.boardService
      .boards()
      .filter(board =>
        !this.boardsInProject().some(
          projectBoard => projectBoard.id === board.id
        )
      )
  );

  /* ----------------------------------------- */
  /* ---------- Handle project view ---------- */
  protected openProjects = signal<{ projectId: string; isOpen: boolean }[]>([]);

  toggleOpenProject(projectId: string) {
    this.openProjects.update(projects => {
      const existing = projects.find(p => p.projectId === projectId);

      if (existing) {
        return projects.map(p =>
          p.projectId === projectId ? { ...p, isOpen: !p.isOpen } : p
        );
      }

      return [...projects, { projectId, isOpen: true }];
    });
  }

  isProjectOpen(projectId: string): boolean {
    return this.openProjects().find(p => p.projectId === projectId)?.isOpen ?? false;
  }

  /* ----------------------------------------------- */
  /* ---------- Handle add board modal ---------- */
  @ViewChild(CreateBoard)
  addBoardModal!: CreateBoard;

  openAddBoardModal() {
    this.addBoardModal.openAddBoardModal();
  }
}
