import { HttpClient } from '@angular/common/http';
import { Service, inject } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { SpaceRequest, SpaceResponse } from '../../models/space';

@Service()
export class SpaceService {
  private http = inject(HttpClient);

  getSpaces(): Observable<SpaceResponse[]> {
    return this.http.get<SpaceResponse[]>(environment.apiUrl + '/space', {
      withCredentials: true,
    });
  }

  getSpace(spaceRequest: SpaceRequest) {
    return this.http.get(environment.apiUrl + '/space/' + spaceRequest.spaceId, {
      withCredentials: true,
    });
  }
}
