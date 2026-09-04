import { Component, input } from '@angular/core';

@Component({
  imports: [],
  selector: 'app-button-primary',
  styleUrl: './button-primary.css',
  templateUrl: './button-primary.html',
})
export class ButtonPrimary {
  label = input("");

}
