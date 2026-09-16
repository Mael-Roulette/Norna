import { HttpClient } from '@angular/common/http';
import { Service, inject } from '@angular/core';
import { Observable } from 'rxjs';
import {
  RemoveSpaceMemberRequest,
  SpaceMemberRequest,
  SpaceMemberWithDetails,
} from '../../models/space';
import { environment } from '../../../environments/environment';

@Service()
export class SpaceMemberService {
  private http = inject(HttpClient);

  addSpaceMember(spaceId: string, request: SpaceMemberRequest): Observable<SpaceMemberWithDetails> {
    return this.http.post<SpaceMemberWithDetails>(
      environment.apiUrl + '/' + spaceId + '/members',
      request,
      {
        withCredentials: true,
      },
    );
  }

  updateSpaceMember(
    spaceId: string,
    request: SpaceMemberRequest,
  ): Observable<SpaceMemberWithDetails> {
    return this.http.patch<SpaceMemberWithDetails>(
      environment.apiUrl + '/' + spaceId + '/members',
      request,
      {
        withCredentials: true,
      },
    );
  }

  removeSpaceMember(spaceId: string, request: RemoveSpaceMemberRequest): Observable<void> {
    return this.http.patch<void>(environment.apiUrl + '/' + spaceId + '/members', request, {
      withCredentials: true,
    });
  }
}
