import { Component, inject, signal } from '@angular/core';
import { FormField, FormRoot, email, form, required } from '@angular/forms/signals';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../service/auth/auth.service';
import { firstValueFrom } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  imports: [FormField, FormRoot, RouterLink],
  selector: 'app-signin',
  styleUrl: './signin.css',
  templateUrl: './signin.html',
})
export class Signin {
  private authService = inject(AuthService);
  private router = inject(Router);

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

            // Redirect to the dashboard
            this.router.navigateByUrl('/dashboard');
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
