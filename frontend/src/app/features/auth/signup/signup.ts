import { Component, signal } from '@angular/core';
import { form, FormField } from '@angular/forms/signals';

@Component({
  imports: [FormField],
  selector: 'app-signup',
  styleUrl: './signup.css',
  templateUrl: './signup.html',
})
export class Signup {
  signupModel = signal({
    username: "",
    email: "",
    password: ""
  });

  signupForm = form( this.signupModel );
}
