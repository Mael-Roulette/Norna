import { HttpClient, httpResource } from '@angular/common/http';
import { Service, computed, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ProjectBoardsUpdateRequest, ProjectNameUpdateRequest, ProjectRequest, ProjectResponse } from '../../models/project';
import { environment } from '../../../environments/environment';
import { BoardService } from '../board/board.service';
import { SpaceService } from '../space/space.service';

@Service()
export class ProjectService {
  private http = inject(HttpClient);
  private spaceService = inject(SpaceService);
  private boardService = inject(BoardService);

  private projectsResource = httpResource<ProjectResponse[]>(() => {
    const space = this.spaceService.actualSpace();

    if ((!this.spaceService.isReady() || !space) && (!this.boardService.isReady() || !this.boardService.boards ) ) {
      return undefined;
    }

    return `${environment.apiUrl}/space/${space.spaceId}/project`;
  });

  readonly projects = computed(() => this.projectsResource.value() ?? []);

  readonly isLoading = this.projectsResource.isLoading;

  readonly error = this.projectsResource.error;

  readonly isReady = computed(
    () => !this.isLoading() && !this.error() && this.projectsResource.value() !== undefined,
  );

  refresh() {
    this.projectsResource.reload();
  }

  getProjects(spaceId: string): Observable<ProjectResponse[]> {
    return this.http.get<ProjectResponse[]>(
      `${environment.apiUrl}/space/${spaceId}/project`,
      {
        withCredentials: true,
      }
    );
  }

  createProject(
    spaceId: string,
    projectRequest: ProjectRequest
  ): Observable<ProjectResponse> {
    return this.http.post<ProjectResponse> (
      `${environment.apiUrl}/space/${spaceId}/project`,
      projectRequest,
      {
        withCredentials: true,
      }
    );
  }

  updateProjectName(
    spaceId: string,
    projectId: string,
    projectNameUpdateRequest: ProjectNameUpdateRequest
  ): Observable<ProjectResponse> {
    return this.http.patch<ProjectResponse>(
      `${environment.apiUrl}/space/${spaceId}/project/${projectId}`,
      projectNameUpdateRequest,
      {
        withCredentials: true
      }
    )
  }

  updateProjectBoards(
    spaceId: string,
    projectId: string,
    projectBoardsUpdateRequest: ProjectBoardsUpdateRequest
  ): Observable<ProjectResponse> {
    return this.http.patch<ProjectResponse>(
      `${environment.apiUrl}/space/${spaceId}/project/${projectId}`,
      projectBoardsUpdateRequest,
      {
        withCredentials: true
      }
    )
  }

  deleteProject(
    spaceId: string,
    projectId: string
  ): Observable<void> {
    return this.http.delete<void>(
      `${environment.apiUrl}/space/${spaceId}/project/${projectId}`,
      {
        withCredentials: true
      }
    )
  }
}
