import { HttpClient } from '@angular/common/http';
import { Service, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { TaskRequest, TaskResponse } from '../../models/task';
import { environment } from '../../../environments/environment';

@Service()
export class TaskService {
  private http = inject(HttpClient);

  createTask( columnId: string, request: TaskRequest ): Observable<TaskResponse> {
    return this.http.post<TaskResponse>(
      `${environment.apiUrl}/tasks`,
      {...request, columnId},
      {
        credentials: 'include',
      },
    )
  }

  updateTask ( taskId: string, columnId: string, request: TaskRequest ): Observable<TaskResponse> {
    return this.http.post<TaskResponse>(
      `${environment.apiUrl}/tasks/${taskId}`,
      {...request, columnId},
      {
        credentials: 'include',
      },
    )
  }

  deleteTask ( taskId: string ): Observable<void> {
    return this.http.delete<void>(
      `${environment.apiUrl}/tasks/${taskId}`,
      {
        credentials: 'include',
      },
    )
  }
}
