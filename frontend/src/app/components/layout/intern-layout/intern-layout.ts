import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter, map, startWith } from 'rxjs';
import { LoadingScreen } from '../../../features/loading-screen/loading-screen';
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
  private router = inject(Router);

  protected spaceId = toSignal(
    this.router.events.pipe(
      filter((e): e is NavigationEnd => e instanceof NavigationEnd),
      startWith(null),
      map(() => this.getLeafParam('spaceId'))
    ),
    { initialValue: this.getLeafParam('spaceId') }
  );

  private getLeafParam(name: string): string | null {
    let r = this.router.routerState.snapshot.root;
    while (r.firstChild) r = r.firstChild;
    return r.paramMap.get(name);
  }
}
