import { Component, inject } from '@angular/core';
import { AppTheme, appThemes } from '../../../../constants/app-theme';
import { ThemeService } from '../../../../service/theme.service';

@Component({
  imports: [],
  selector: 'app-appearance-settings',
  styleUrl: './appearance-settings.css',
  templateUrl: './appearance-settings.html',
})
export class AppearanceSettings {
  protected themeService = inject(ThemeService);
  appThemes = appThemes;

  /* ---------------------------------- */
  /* ---------- Update theme ---------- */
  updatePreferredTheme(theme: AppTheme) {
    this.themeService.setPreference(theme);
  }
}
