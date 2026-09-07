import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ThemeService } from './service/theme.service';

@Component({
  imports: [RouterOutlet],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('frontend');
  private readonly themeService = inject(ThemeService);
  
}
