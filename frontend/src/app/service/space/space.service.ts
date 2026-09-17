import { HttpClient, httpResource } from '@angular/common/http';
import { Service, computed, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SpaceResponse, SpaceResponseWithDetails, spaceRequest } from '../../models/space';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Service()
export class SpaceService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);

  private spacesResource = httpResource<SpaceResponse[]>(() =>
    this.authService.isSessionRestored() && this.authService.isLoggedIn()
      ? `${environment.apiUrl}/space`
      : undefined,
  );

  readonly spaces = computed(() => this.spacesResource.value() ?? []);

  readonly isLoading = this.spacesResource.isLoading;

  readonly error = this.spacesResource.error;

  readonly isReady = computed(
    () => !this.isLoading() && !this.error() && this.spacesResource.value() !== undefined,
  );

  refresh() {
    this.spacesResource.reload();
  }

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
