import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
import { catchError, switchMap, throwError } from 'rxjs';

/**
 * Attaches the stored bearer token, if any, to every outgoing request.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getAccessToken();

  // Don't attach an access token to public/authentication endpoints
  const isPublicAuthRoute =
    req.url.endsWith('/auth/signin') ||
    req.url.endsWith('/auth/signup') ||
    req.url.endsWith('/auth/refresh');

  const authReq =
    token && !isPublicAuthRoute
      ? req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
          },
        })
      : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status !== 401 || isPublicAuthRoute) {
        return throwError(() => error);
      }

      return authService.restoreSession().pipe(
        switchMap((refreshed) => {
          if (!refreshed) {
            return throwError(() => error);
          }

          const newToken = authService.getAccessToken();

          if (!newToken) {
            return throwError(() => error);
          }

          const retriedReq = req.clone({
            setHeaders: {
              Authorization: `Bearer ${newToken}`,
            },
          });

          return next(retriedReq);
        }),
      );
    }),
  );
};
