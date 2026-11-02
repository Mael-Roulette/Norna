import { Routes } from '@angular/router';
import { Signin } from './features/auth/signin/signin';
import { Signup } from './features/auth/signup/signup';
import { Dashboard } from './features/dashboard/dashboard';
import { authGuard } from './guards/auth-guard';
import { InternLayout } from './components/layout/intern-layout/intern-layout';
import { DashboardRedirect } from './features/dashboard/dashboard-redirect/dashboard-redirect';
import { Board } from './features/board/board';
import { Settings } from './features/settings/settings';

export const routes: Routes = [
  { path: '', component: DashboardRedirect },
  { path: 'dashboard', component: DashboardRedirect },
  { path: 'dashboard/:spaceId', redirectTo: 'space/:spaceId/dashboard' },
  { path: 'board/:boardId', component: DashboardRedirect },
  {
    path: 'auth',
    children: [
      { path: 'sign-in', component: Signin, title: 'Sign in page' },
      { path: 'sign-up', component: Signup, title: 'Sign up page' },
    ],
  },
  {
    path: 'space/:spaceId',
    component: InternLayout,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: Dashboard, title: 'Dashboard page' },
      { path: 'board/:boardId', component: Board, title: 'Board page' },
      { path: 'settings', component: Settings, title: 'Settings page' },
    ],
  },
  { path: '**', component: DashboardRedirect },
];