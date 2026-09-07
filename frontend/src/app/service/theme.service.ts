import { Injectable, signal, effect } from '@angular/core';

type Theme = 'light' | 'dark';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  // Storage key for the selected theme
  private readonly storageKey = 'theme';

  readonly theme = signal<Theme>(this.getInitialTheme());

  constructor() {
    // Apply .dark class on <html> tag
    effect(() => {
      const isDark = this.theme() === 'dark';
      document.documentElement.classList.toggle('dark', isDark);
      localStorage.setItem(this.storageKey, this.theme());
    });
  }

  toggle(): void {
    this.theme.update((t) => (t === 'dark' ? 'light' : 'dark'));
  }

  setTheme(theme: Theme): void {
    this.theme.set(theme);
  }

  private getInitialTheme(): Theme {
    const stored = localStorage.getItem(this.storageKey) as Theme | null;
    if (stored === 'dark' || stored === 'light') return stored;

    return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
  }
}
