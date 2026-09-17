import { Component, inject } from '@angular/core';
import { Header } from '../header/header';
import { Sidebar } from '../sidebar/sidebar';
import { RouterOutlet } from '@angular/router';
import { UserService } from '../../../service/user/user.service';
import { SpaceService } from '../../../service/space/space.service';
import { LoadingScreen } from '../../../features/loading-screen/loading-screen';

@Component({
  imports: [Header, Sidebar, RouterOutlet, LoadingScreen],
  selector: 'app-intern-layout',
  styleUrl: './intern-layout.css',
  templateUrl: './intern-layout.html',
})
export class InternLayout {
  protected userService = inject(UserService);
  protected spaceService = inject(SpaceService);
}
