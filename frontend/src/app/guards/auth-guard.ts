import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map, catchError, of } from 'rxjs';
import { AuthService } from '../service/auth/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  }

  // try to restore the session with the refresh token
  return authService.restoreSession().pipe(
    map((restored) => restored || router.createUrlTree(['/sign-in'])),
    catchError(() => of(router.createUrlTree(['/sign-in']))),
  );
};
