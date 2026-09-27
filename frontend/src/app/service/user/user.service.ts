import { Service, computed, inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { HttpClient, httpResource } from '@angular/common/http';
import { UpdateLastVisitedSpaceRequest, UpdateUsernameRequest, UserResponse } from '../../models/user';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Service()
export class UserService {
  private authService = inject(AuthService);
  private http = inject(HttpClient);

  private userResource = httpResource<UserResponse>(() =>
    this.authService.isSessionRestored() && this.authService.isLoggedIn()
      ? `${environment.apiUrl}/auth/me`
      : undefined,
  );

  readonly user = computed(() => this.userResource.value() ?? null);

  requireUser(): UserResponse {
    const user = this.user();
    if (!user) throw new Error('UserService.requireUser() called before user was ready');
    return user;
  }

  readonly isLoading = this.userResource.isLoading;
  readonly error = this.userResource.error;
  readonly isReady = computed(
    () => !this.userResource.isLoading() && !this.userResource.error() && this.user() !== null,
  );

  updateLastVisitedSpace(request: UpdateLastVisitedSpaceRequest): Observable<UserResponse> {
    return this.http.patch<UserResponse>(environment.apiUrl + '/me/last-visited-space', request, {
      withCredentials: true,
    });
  }

  updateUsername(request: UpdateUsernameRequest): Observable<UserResponse> {
    return this.http.patch<UserResponse>(
      environment.apiUrl + '/me/username',
      request,
      {
        withCredentials: true
      }
    )
  }

  refresh() {
    this.userResource.reload();
  }
}
