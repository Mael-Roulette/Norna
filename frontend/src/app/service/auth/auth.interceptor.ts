import { HttpInterceptorFn } from '@angular/common/http';

/**
 * Attaches the stored bearer token, if any, to every outgoing request.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Get the token from the local storage
  const idToken = localStorage.getItem('id_token');

  // if no token, we send the original request
  if (!idToken) {
    return next(req);
  }

  // Clone the request and add Bearer authorization with the token
  const cloned = req.clone({
    headers: req.headers.set('Authorization', `Bearer ${idToken}`),
  });

  // Return the cloned request
  return next(cloned);
};
