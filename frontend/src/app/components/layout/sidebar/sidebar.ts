import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgIcon, provideIcons } from '@ng-icons/core';
import {
  lucideHome,
  lucideFolderOpen,
  lucideFolder,
  lucideChevronUp,
  lucideChevronDown,
  lucideUsersRound,
  lucideSettings,
} from '@ng-icons/lucide';
import { phosphorSuitcaseBold } from '@ng-icons/phosphor-icons/bold';

@Component({
  imports: [RouterLink, NgIcon],
  providers: [
    provideIcons({
      lucideHome,
      phosphorSuitcaseBold,
      lucideFolderOpen,
      lucideFolder,
      lucideChevronUp,
      lucideChevronDown,
      lucideUsersRound,
      lucideSettings,
    }),
  ],
  selector: 'app-sidebar',
  styleUrl: './sidebar.css',
  templateUrl: './sidebar.html',
})
export class Sidebar {}
