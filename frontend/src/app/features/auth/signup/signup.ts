import { Component, inject, signal } from '@angular/core';
import { FormField, FormRoot, email, form, maxLength, minLength, pattern, required } from '@angular/forms/signals';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../../../service/auth.service';

@Component({
  imports: [FormField, FormRoot],
  selector: 'app-signup',
  styleUrl: './signup.css',
  templateUrl: './signup.html',
})
export class Signup {
  private authService = inject(AuthService);

  signupModel = signal({
    username: "",
    email: "",
    password: ""
  });

  signupForm = form(
    this.signupModel,
    (schemaPath) => {
      // Username validation
      required(schemaPath.username, {message: 'Username is required'});
      minLength(schemaPath.username, 3, {message: 'Username must be at least 3 chars.'});
      maxLength(schemaPath.username, 50, {message: 'Username must be under 50 chars.'});

      // Email validation
      required(schemaPath.email, {message: 'Email is required'});
      email(schemaPath.email, {message: 'Enter a valid email address'});

      // Password validation
      required(schemaPath.password, { message: 'Password is required'});
      minLength(schemaPath.password, 8, {message: 'Password must be at least 8 chars.'});
      // Password must contain at least one digit, one lowercase letter and one uppercase letter
      pattern(schemaPath.password, /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).*$/, {message: "Must contain at least one number and one uppercase and lowercase letter"})
    },
    {
      submission: {
        action: async () => {
          try {
            // Read the latest values from the form model
            const userRequest = this.signupModel();

            // Send the signup request to the backend
            const result = await firstValueFrom(
              this.authService.signupUser(userRequest)
            );

            console.log('Signup successful:', result);
          } catch (error) {
            // TODO: Replace console logging with user-facing error handling
            console.error('Signup failed:', error);
          }
        }
      }
    }
  );
}
