import { Component } from '@angular/core';

@Component({
  selector: 'countries',
  templateUrl: './cities.component.html',
})
export class CitiesComponent {
  cities = [{name: "Wrocław", id: 1}]
}
