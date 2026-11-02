import { Component, inject, input } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { LoadingScreen } from '../../../features/loading-screen/loading-screen';
import { BoardService } from '../../../service/board/board.service';
import { SpaceService } from '../../../service/space/space.service';
import { UserService } from '../../../service/user/user.service';
import { Header } from '../header/header';
import { Sidebar } from '../sidebar/sidebar';

@Component({
  imports: [Header, Sidebar, RouterOutlet, LoadingScreen],
  selector: 'app-intern-layout',
  styleUrl: './intern-layout.css',
  templateUrl: './intern-layout.html',
})
export class InternLayout {
  protected userService = inject(UserService);
  protected spaceService = inject(SpaceService);
  protected boardService = inject(BoardService);

  spaceId = input.required<string>();
}
