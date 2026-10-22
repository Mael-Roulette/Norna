import { HttpClient } from '@angular/common/http';
import { Injectable, signal, effect, inject } from '@angular/core';
import { UpdatePreferredThemeRequest, UserResponse } from '../models/user';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UserService } from './user/user.service';
import { AppTheme } from '../constants/app-theme';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private http = inject(HttpClient);
  private userService = inject(UserService);

  private readonly storageKey = 'theme';
  private readonly mediaQuery =
    typeof window !== 'undefined' && typeof window.matchMedia === 'function'
      ? window.matchMedia('(prefers-color-scheme: dark)')
      : null;

  readonly preference = signal<AppTheme>(this.getInitialPreference());

  readonly resolvedTheme = signal<'dark' | 'light'>(this.resolveTheme(this.preference()));

  private syncingFromUser = false;

  constructor() {
    effect(() => {
      document.documentElement.classList.toggle('dark', this.resolvedTheme() === 'dark');
    });

    effect(() => {
      return localStorage.setItem( this.storageKey, this.preference() );
    });

    effect(() => {
      this.resolvedTheme.set(this.resolveTheme(this.preference()));
    });

    this.mediaQuery?.addEventListener('change', () => {
      if (this.preference() === AppTheme.SYSTEM) {
        this.resolvedTheme.set(this.mediaQuery!.matches ? 'dark' : 'light');
      }
    });

    effect(() => {
      const user = this.userService.user();
      if (!user) return;

      this.syncingFromUser = true;
      this.preference.set(user.preferredTheme);
      this.syncingFromUser = false;
    });
  }

  toggle(): void {
    this.setPreference(this.resolvedTheme() === 'dark' ? AppTheme.LIGHT : AppTheme.DARK);
  }

  setPreference(newPreferredTheme: AppTheme): void {
    const previousPreference = this.preference();

    this.preference.set(newPreferredTheme);

    const user = this.userService.user();

    if (!user || user.preferredTheme === newPreferredTheme) {
      return;
    }

    this.updatePreferredTheme({ newPreferredTheme }).subscribe({
      error: () => {
        this.preference.set(previousPreference);
      }
    });
  }

  private resolveTheme(pref: AppTheme): 'dark' | 'light' {
    if (pref === AppTheme.SYSTEM) {
      return this.mediaQuery?.matches ? 'dark' : 'light';
    }
    return pref === AppTheme.DARK ? 'dark' : 'light';
  }

  private getInitialPreference(): AppTheme {
    const stored = localStorage.getItem(this.storageKey) as AppTheme | null;
    if (stored === AppTheme.DARK || stored === AppTheme.LIGHT || stored === AppTheme.SYSTEM) {
      return stored;
    }
    return AppTheme.SYSTEM;
  }

  updatePreferredTheme(request: UpdatePreferredThemeRequest): Observable<UserResponse> {
    return this.http.patch<UserResponse>(
      environment.apiUrl + '/me/preferred-theme',
      request,
      { withCredentials: true }
    );
  }
}