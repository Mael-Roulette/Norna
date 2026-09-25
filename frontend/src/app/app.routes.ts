import { Routes } from '@angular/router';
import { Signin } from './features/auth/signin/signin';
import { Signup } from './features/auth/signup/signup';
import { Dashboard } from './features/dashboard/dashboard';
import { authGuard } from './guards/auth-guard';
import { InternLayout } from './components/layout/intern-layout/intern-layout';
import { DashboardRedirect } from './features/dashboard/dashboard-redirect/dashboard-redirect';
import { Board } from './features/board/board';

export const routes: Routes = [
  {
    path: '',
    component: DashboardRedirect,
  },
  {
    path: 'dashboard',
    component: DashboardRedirect,
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
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard/:spaceId',
        component: Dashboard,
        title: 'Dashboard page',
      },
      {
        path: 'board/:boardId',
        component: Board,
        title: 'Board page',
      }
    ],
  },
];
