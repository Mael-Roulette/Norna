import { HttpClient } from '@angular/common/http';
import { Service, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SpaceResponse, SpaceResponseWithDetails, spaceRequest } from '../../models/space';

@Service()
export class SpaceService {
  private http = inject(HttpClient);

  getSpaces(): Observable<SpaceResponse[]> {
    return this.http.get<SpaceResponse[]>(environment.apiUrl + '/space', {
      withCredentials: true,
    });
  }

  getSpace(spaceId: string) {
    return this.http.get(environment.apiUrl + '/space/' + spaceId, {
      withCredentials: true,
    });
  }

  createSpace(spaceRequest: spaceRequest): Observable<SpaceResponseWithDetails> {
    return this.http.post<SpaceResponseWithDetails>(environment.apiUrl + '/space', spaceRequest, {
      withCredentials: true,
    });
  }

  updateSpace(spaceId: string, spaceRequest: spaceRequest): Observable<SpaceResponseWithDetails> {
    return this.http.patch<SpaceResponseWithDetails>(
      environment.apiUrl + '/space/' + spaceId,
      spaceRequest,
      {
        withCredentials: true,
      },
    );
  }

  deleteSpace(spaceId: string): Observable<void> {
    return this.http.delete<void>(environment.apiUrl + '/space/' + spaceId, {
      withCredentials: true,
    });
  }
}
