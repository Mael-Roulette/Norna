import { Routes } from '@angular/router';
import { Signin } from './features/auth/signin/signin';
import { Signup } from './features/auth/signup/signup';
import { Dashboard } from './features/dashboard/dashboard';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: 'sign-in',
    component: Signin,
    title: 'Sign in page',
  },
  {
    path: 'sign-up',
    component: Signup,
    title: 'Sign up page',
  },
  {
    path: 'dashboard',
    component: Dashboard,
    title: 'Dashboard page',
    canActivate: [authGuard],
  },
];
