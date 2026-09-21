import { HttpClient, httpResource } from '@angular/common/http';
import { Service, computed, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { BoardRequest, BoardResponse } from '../../models/board';
import { SpaceService } from '../space/space.service';

@Service()
export class BoardService {
  private http = inject(HttpClient);
  private spaceService = inject(SpaceService);

  private boardsResource = httpResource<BoardResponse[]>(() => {
    const space = this.spaceService.actualSpace();

    if (!this.spaceService.isReady() || !space) {
      return undefined;
    }

    return `${environment.apiUrl}/space/${space.spaceId}/board`;
  });

  readonly boards = computed(() => this.boardsResource.value() ?? []);

  readonly isLoading = this.boardsResource.isLoading;

  readonly error = this.boardsResource.error;

  readonly isReady = computed(
    () => !this.isLoading() && !this.error() && this.boardsResource.value() !== undefined,
  );

  refresh() {
    this.boardsResource.reload();
  }

  getBoards(spaceId: string): Observable<BoardResponse[]> {
    return this.http.get<BoardResponse[]>(
      `${environment.apiUrl}/space/${spaceId}/board/`,
      {
        withCredentials: true,
      },
    );
  }

  createBoard(
    spaceId: string,
    boardRequest: BoardRequest
  ): Observable<BoardResponse> {
    return this.http.post<BoardResponse>(
      `${environment.apiUrl}/space/${spaceId}/board/`,
      boardRequest,
      {
        withCredentials: true,
      }
    );
  }

  updateBoard(
    spaceId: string,
    boardId: string,
    boardRequest: BoardRequest
  ): Observable<BoardResponse> {
    return this.http.patch<BoardResponse>(
      `${environment.apiUrl}/space/${spaceId}/board/${boardId}`,
      boardRequest,
      {
        withCredentials: true,
      }
    )
  }

  deleteBoard(
    spaceId: string,
    boardId: string
  ): Observable<void> {
    return this.http.delete<void>(
      `${environment.apiUrl}/space/${spaceId}/board/${boardId}`,
      {
        withCredentials: true
      }
    )
  }
}
