import { Component, inject, signal } from '@angular/core';
import {
  FormField,
  FormRoot,
  email,
  form,
  maxLength,
  minLength,
  pattern,
  required,
} from '@angular/forms/signals';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../../../service/auth/auth.service';
import { Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { phosphorEyeBold, phosphorEyeClosedBold } from '@ng-icons/phosphor-icons/bold';
import { provideIcons, NgIcon } from '@ng-icons/core';

@Component({
  imports: [FormField, FormRoot, RouterLink, NgIcon],
  providers: [provideIcons({phosphorEyeBold, phosphorEyeClosedBold})],
  selector: 'app-signup',
  styleUrl: './signup.css',
  templateUrl: './signup.html',
})
export class Signup {
  private authService = inject(AuthService);
  private router = inject(Router);

  protected readonly showPassword = signal(false);

  toggleShowPassword () {
    this.showPassword.update(v => !v);
  }

  signupError = signal<string | null>(null);

  signupModel = signal({
    username: '',
    email: '',
    password: '',
  });

  signupForm = form(
    this.signupModel,
    (schemaPath) => {
      // Username validation
      required(schemaPath.username, { message: 'Username is required' });
      minLength(schemaPath.username, 3, { message: 'Username must be at least 3 chars.' });
      maxLength(schemaPath.username, 50, { message: 'Username must be under 50 chars.' });

      // Email validation
      required(schemaPath.email, { message: 'Email is required' });
      email(schemaPath.email, { message: 'Enter a valid email address' });

      // Password validation
      required(schemaPath.password, { message: 'Password is required' });
      minLength(schemaPath.password, 8, { message: 'Password must be at least 8 chars.' });
      // Password must contain at least one digit, one lowercase letter and one uppercase letter
      pattern(schemaPath.password, /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).*$/, {
        message: 'Must contain at least one number and one uppercase and lowercase letter',
      });
    },
    {
      submission: {
        action: async () => {
          try {
            // Read the latest values from the form model
            const userRequest = this.signupModel();

            // Send the signup request to the backend
            await firstValueFrom(this.authService.signupUser(userRequest));

            // redirect to sign in page
            this.router.navigate(
              ['/sign-in'],
              { queryParams: { registered: true } }
            );
          } catch (error: unknown) {
            if (error instanceof HttpErrorResponse) {
              switch (error.status) {
                case 409:
                  this.signupError.set(
                    error.error?.message ?? 'Username or email is already in use.',
                  );
                  break;
                case 400:
                  this.signupError.set(
                    error.error?.message ?? 'Please check the information provided.',
                  );
                  break;
                case 500:
                  this.signupError.set(
                    'Something went wrong on the server. Please try again later.',
                  );
                  break;
                case 0:
                  this.signupError.set(
                    'Unable to connect to the server. Please check your connection.',
                  );
                  break;
                default:
                  this.signupError.set(
                    error.error?.message ?? 'An error occurred. Please try again.',
                  );
              }
              return;
            }
            this.signupError.set('An unexpected error occurred. Please try again.');
          }
        },
      },
    },
  );
}
