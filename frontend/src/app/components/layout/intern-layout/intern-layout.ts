import { Component } from '@angular/core';
import { Header } from '../header/header';
import { Sidebar } from '../sidebar/sidebar';
import { RouterOutlet } from '@angular/router';

@Component({
  imports: [Header, Sidebar, RouterOutlet],
  selector: 'app-intern-layout',
  styleUrl: './intern-layout.css',
  templateUrl: './intern-layout.html',
})
export class InternLayout {}
