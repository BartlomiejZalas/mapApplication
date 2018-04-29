import { Component } from '@angular/core';
import {ExtraColumn} from "../model/extracolumn";

@Component({
  selector: 'countries',
  templateUrl: './cities.component.html',
})
export class CitiesComponent {
  extraColumns = [
    new ExtraColumn('continentName', 'Continent Name'),
    new ExtraColumn('countryName', 'Country Name')
  ];
}
