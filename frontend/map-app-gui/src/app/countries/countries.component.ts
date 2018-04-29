import { Component } from '@angular/core';
import {ExtraColumn} from "../model/extracolumn";

@Component({
  selector: 'countries',
  templateUrl: './countries.component.html',
})
export class CountriesComponent {
  extraColumns = [new ExtraColumn('continentName', 'Continent Name')];
}
