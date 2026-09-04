import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Service, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { UserRequest, UserResponse } from '../models/user';

@Service()
export class AuthService {
  // TODO: Change the location of the url into environment variable file
  readonly baseUrl = "http://localhost:8080/api/v1";

  private http = inject(HttpClient);

  signupUser( userRequest: UserRequest ): Observable<UserResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http
      .post<UserResponse>(
        this.baseUrl + '/auth/signup',
        userRequest,
        {
          headers,
          withCredentials: true
        }
      );
  }
}
