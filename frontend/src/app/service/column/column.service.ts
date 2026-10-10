import { HttpClient } from '@angular/common/http';
import { Service, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { BoardColumnRequest, BoardColumnResponse, updateColumnNameRequest, updateColumnPositionRequest } from '../../models/column';
import { environment } from '../../../environments/environment';

@Service()
export class ColumnService {
  private http = inject(HttpClient);

  getBoardColumns( boardId: string ): Observable<BoardColumnResponse[]> {
    return this.http.get<BoardColumnResponse[]>(
      `${environment.apiUrl}/board/${boardId}/column`,
        {
          credentials: "include",
        },
    )
  }

  createColumn( boardId: string, request: BoardColumnRequest ): Observable<BoardColumnResponse> {
    return this.http.post<BoardColumnResponse>(
      `${environment.apiUrl}/board/${boardId}/column`,
      request,
      {
        credentials: "include",
      },
    )
  }

  updateColumnName( boardId: string, columnId: string, request: updateColumnNameRequest ): Observable<BoardColumnResponse> {
    return this.http.patch<BoardColumnResponse>(
      `${environment.apiUrl}/board/${boardId}/column/${columnId}/name`,
      request,
      {
        credentials: "include",
      },
    )
  }

    updateColumnPosition( boardId: string, columnId: string, request: updateColumnPositionRequest ): Observable<BoardColumnResponse> {
    return this.http.patch<BoardColumnResponse>(
      `${environment.apiUrl}/board/${boardId}/column/${columnId}/position`,
      request,
      {
        credentials: "include",
      },
    )
  }

  deleteColumn( boardId: string, columnId: string ): Observable<void> {
    return this.http.delete<void>(
      `${environment.apiUrl}/board/${boardId}/column/${columnId}`,
      {
        credentials: "include",
      },
    )
  }
}
