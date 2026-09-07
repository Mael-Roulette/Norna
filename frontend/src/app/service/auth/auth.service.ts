import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Service, inject } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { UserResponse, UserSigninRequest, UserSignupRequest } from '../../models/user';
import moment from 'moment';
import { AuthResult } from '../../models/auth';

@Service()
export class AuthService {
  // TODO: Change the location of the url into environment variable file
  readonly baseUrl = 'http://localhost:8080/api/v1';

  private http = inject(HttpClient);

  signupUser(userRequest: UserSignupRequest): Observable<UserResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<UserResponse>(this.baseUrl + '/auth/signup', userRequest, {
      headers,
      withCredentials: true,
    });
  }

  signinUser(userRequest: UserSigninRequest): Observable<AuthResult> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http
      .post<AuthResult>(this.baseUrl + '/auth/signin', userRequest, {
        headers,
        withCredentials: true,
      })
      .pipe(tap((authResult) => this.setSession(authResult)));
  }

  logout() {
    localStorage.removeItem('id_token');
    localStorage.removeItem('expires_at');
  }

  private setSession(authResult: AuthResult) {
    // Get the moment where the token expires
    const expiresAt = moment().add(authResult.expiresIn, 'second');

    // Set the value of the token and the expiresAt in local storage
    localStorage.setItem('id_token', authResult.token);
    localStorage.setItem('expires_at', JSON.stringify(expiresAt.valueOf()));
  }

  public isLoggedIn() {
    return moment().isBefore(this.getExpiration());
  }

  isLoggedOut() {
    return !this.isLoggedIn();
  }

  getExpiration() {
    const expiration = localStorage.getItem('expires_at');
    if (!expiration) {
      return moment(0);
    }

    const expiresAt = JSON.parse(expiration);
    return moment(expiresAt);
  }
}
