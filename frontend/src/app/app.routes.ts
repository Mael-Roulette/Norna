import { Routes } from '@angular/router';
import { Signin } from './features/auth/signin/signin';
import { Signup } from './features/auth/signup/signup';
import { Dashboard } from './features/dashboard/dashboard';
import { authGuard } from './guards/auth-guard';
import { InternLayout } from './components/layout/intern-layout/intern-layout';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: 'auth',
    children: [
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
    ],
  },
  {
    path: '',
    component: InternLayout,
    children: [
      {
        path: 'dashboard',
        component: Dashboard,
        title: 'Dashboard page',
        canActivate: [authGuard],
      },
    ],
  },
];
