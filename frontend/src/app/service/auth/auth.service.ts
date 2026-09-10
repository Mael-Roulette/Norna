import { HttpClient } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';
import { Observable, catchError, map, of, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResult } from '../../models/auth';
import { UserResponse, UserSigninRequest, UserSignupRequest } from '../../models/user';

@Service()
export class AuthService {
  private http = inject(HttpClient);

  // Access token only keep in memory
  private accessToken = signal<string | null>(null);
  private expiresAt = signal<number>(0);

  private sessionRestored = signal(false);
  readonly isSessionRestored = computed(() => this.sessionRestored());

  readonly isLoggedIn = computed(() => Date.now() < this.expiresAt());

  signupUser(userRequest: UserSignupRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(environment.apiUrl + '/auth/signup', userRequest, {
      withCredentials: true,
    });
  }

  signinUser(userRequest: UserSigninRequest): Observable<AuthResult> {
    return this.http
      .post<AuthResult>(environment.apiUrl + '/auth/signin', userRequest, {
        withCredentials: true,
      })
      .pipe(
        tap((authResult) => {
          this.setSession(authResult);
          this.sessionRestored.set(true);
        }),
      );
  }

  /**
   * called when the app starts to restore the session via the httpOnly cookie
   * as the access token in memory is lost every time the page is reloaded
   */
  restoreSession(): Observable<boolean> {
    return this.http
      .post<AuthResult>(`${environment.apiUrl}/auth/refresh`, {}, { withCredentials: true })
      .pipe(
        tap((authResult) => {
          this.setSession(authResult);
          this.sessionRestored.set(true);
        }),
        map(() => true),
        catchError(() => {
          this.clearSession();
          this.sessionRestored.set(true);
          return of(false);
        }),
      );
  }

  logout(): Observable<void> {
    return this.http
      .post<void>(`${environment.apiUrl}/auth/logout`, {}, { withCredentials: true })
      .pipe(tap(() => this.clearSession()));
  }

  getAccessToken(): string | null {
    return this.accessToken();
  }

  private setSession(authResult: AuthResult) {
    this.accessToken.set(authResult.token);
    this.expiresAt.set(Date.now() + authResult.expiresIn * 1000);
  }

  private clearSession() {
    this.accessToken.set(null);
    this.expiresAt.set(0);
  }
}
