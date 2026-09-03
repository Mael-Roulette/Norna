import { Component, signal } from '@angular/core';
import { FormField, form } from '@angular/forms/signals';

@Component({
  imports: [FormField],
  selector: 'app-signin',
  styleUrl: './signin.css',
  templateUrl: './signin.html',
})
export class Signin {
  signinModel = signal({
    username: "",
    email: "",
    password: ""
  });

  signinForm = form( this.signinModel );
}
