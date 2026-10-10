import { Component, inject } from '@angular/core';
import { UserService } from '../../service/user/user.service';

@Component({
  imports: [],
  selector: 'app-dashboard',
  styleUrl: './dashboard.css',
  templateUrl: './dashboard.html',
})
export class Dashboard {
  protected userService = inject(UserService);
}
