import { Service, computed, inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { httpResource } from '@angular/common/http';
import { UserResponse } from '../../models/user';
import { environment } from '../../../environments/environment';

@Service()
export class UserService {
  private authService = inject(AuthService);

  private userResource = httpResource<UserResponse>(() =>
    this.authService.isSessionRestored() && this.authService.isLoggedIn()
      ? `${environment.apiUrl}/auth/me`
      : undefined,
  );

  readonly user = computed(() => this.userResource.value() ?? null);

  readonly isLoading = this.userResource.isLoading;
  readonly error = this.userResource.error;

  refresh() {
    this.userResource.reload();
  }
}
