import {Component, OnInit} from '@angular/core';
import {LocationsService} from "../services/locations.service";
import {Continent} from "../model/continent";

@Component({
  selector: 'cities.add',
  templateUrl: './cities.add.component.html'
})
export class CitiesAddComponent implements OnInit {

  saved: boolean;
  message: string;
  errors: string[] = [];
  countries: Continent[];
  loaded: boolean;

  constructor(private locationsService: LocationsService) {
  }

  ngOnInit(): void {
    this.locationsService.getLocations('countries').subscribe(countries => {
      this.countries = countries;
      console.log(this.countries);
      this.loaded = true;
    });
  }

  save(name: string, countryId: string): void {
    console.log(name, countryId);
    this.errors = [];
    if (name == '') {
      this.errors.push("Country name cannot be empty!");
      return
    }
    if (countryId == '') {
      this.errors.push("Country cannot be empty!");
      return
    }
    this.locationsService.addCity(name, Number(countryId)).subscribe(
      city => {this.saved = true; this.message = 'City with id: ' + city.id + ' saved!'},
      err => this.errors.push("Save error: " + err)
    );
  }


}



