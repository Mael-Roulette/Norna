import { Routes } from '@angular/router';
import { Signin } from './features/auth/signin/signin';
import { Signup } from './features/auth/signup/signup';
import { Dashboard } from './features/dashboard/dashboard';

export const routes: Routes = [
  {
    path: 'sign-in',
    component: Signin,
    title: 'Sign in page'
  },
  {
    path: 'sign-up',
    component: Signup,
    title: 'Sign up page'
  },
  {
    path: '',
    component: Dashboard,
    title: 'Dashboard page'
  },
];
