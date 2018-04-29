import { Component } from '@angular/core';

@Component({
  selector: 'countries',
  templateUrl: './countries.component.html',
})
export class CountriesComponent {
  countries = [{name: "Poland", id: 1}]
}
