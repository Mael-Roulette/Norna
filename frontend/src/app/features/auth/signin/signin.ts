import { Component, Injector, OnInit, computed, effect, inject, signal } from '@angular/core';
import { FormField, FormRoot, email, form, required } from '@angular/forms/signals';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../service/auth/auth.service';
import { TimeoutError, filter, firstValueFrom, take, timeout } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { phosphorEyeBold, phosphorEyeClosedBold } from '@ng-icons/phosphor-icons/bold';
import { provideIcons, NgIcon } from '@ng-icons/core';
import { UserService } from '../../../service/user/user.service';
import { SpaceService } from '../../../service/space/space.service';
import { toObservable } from '@angular/core/rxjs-interop';

@Component({
  imports: [FormField, FormRoot, RouterLink, NgIcon],
  providers: [provideIcons({ phosphorEyeBold, phosphorEyeClosedBold })],
  selector: 'app-signin',
  styleUrl: './signin.css',
  templateUrl: './signin.html',
})
export class Signin implements OnInit {
  private route = inject(ActivatedRoute);
  private authService = inject(AuthService);
  private router = inject(Router);
  private userService = inject(UserService);
  private spaceService = inject(SpaceService);
  private injector = inject(Injector);

  protected readonly registered = signal(false);
  ngOnInit() {
    this.registered.set(this.route.snapshot.queryParams['registered'] === 'true');
  }

  /* ------------------------------------- */
  /* ---------- Toggle password ---------- */
  protected readonly showPassword = signal(false);
  toggleShowPassword() {
    this.showPassword.update((v) => !v);
  }

  /* ---------------------------------- */
  /* ---------- Sign in form ---------- */
  private readonly sessionStatus = computed(() => {
    const userReady = this.userService.isReady();
    const spacesReady = this.spaceService.isReady();
    const userError = this.userService.error();
    const spacesError = this.spaceService.error();

    if (userError) return { status: 'error' as const, error: userError };
    if (spacesError) return { status: 'error' as const, error: spacesError };
    if (userReady && spacesReady) return { status: 'ready' as const };
    return { status: 'pending' as const };
  });

  private async waitForSessionData(timeoutMs = 10000): Promise<void> {
    const status$ = toObservable(this.sessionStatus, { injector: this.injector }).pipe(
      filter((s) => s.status !== 'pending'),
      take(1),
      timeout(timeoutMs),
    );

    try {
      const s_1 = await firstValueFrom( status$ );
      if ( s_1.status === 'error' ) {
        throw s_1.error;
      }
    } catch ( err ) {
      if ( err instanceof TimeoutError ) {
        throw new Error( 'The session data has expired. Please try again.' );
      }
      throw err;
    }
  }

  signinError = signal<string | null>(null);

  signinModel = signal({
    email: '',
    password: '',
  });

  signinForm = form(
    this.signinModel,
    (schemaPath) => {
      // Email validation
      required(schemaPath.email, { message: 'Email is required' });
      email(schemaPath.email, { message: 'Enter a valid email address' });

      // Password validation
      required(schemaPath.password, { message: 'Password is required' });
    },
    {
      submission: {
        action: async () => {
          try {
            // Read the latest values from the form model
            const userRequest = this.signinModel();

            // Send the signup request to the backend
            await firstValueFrom(this.authService.signinUser(userRequest));

            await this.waitForSessionData();

            const actualSpace = this.spaceService.actualSpace();

            if (!actualSpace) {
              this.signinError.set('No space is available for this account.');
              return;
            }

            // Redirect to the dashboard
            this.router.navigateByUrl(`/dashboard/${actualSpace.spaceId}`);
          } catch (error: unknown) {
            if (error instanceof HttpErrorResponse) {
              if (error.status === 401) {
                this.signinError.set(error.error?.message ?? 'Incorrect email address or password');
                return;
              }

              this.signinError.set('An error has occurred. Please try again.');

              return;
            }

            this.signinError.set('An unexpected error has occurred.');
          }
        },
      },
    },
  );
}
