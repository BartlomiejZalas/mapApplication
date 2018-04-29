import {Component, OnInit} from '@angular/core';
import {LocationsService} from "../services/locations.service";
import {Continent} from "../model/continent";

@Component({
  selector: 'countries.add',
  templateUrl: './countries.add.component.html'
})
export class CountriesAddComponent implements OnInit {

  saved: boolean;
  message: string;
  errors: string[] = [];
  continents: Continent[];
  loaded: boolean;

  constructor(private locationsService: LocationsService) {
  }

  ngOnInit(): void {
    this.locationsService.getLocations('continents').subscribe(continents => {
      this.continents = continents;
      console.log(this.continents);
      this.loaded = true;
    });
  }

  save(name: string, continentId: string): void {
    console.log(name, continentId);
    this.errors = [];
    if (name == '') {
      this.errors.push("Country name cannot be empty!");
      return
    }
    if (continentId == '') {
      this.errors.push("Continent cannot be empty!");
      return
    }
    this.locationsService.addCountry(name, Number(continentId)).subscribe(country => {
      this.saved = true;
      this.message = 'Country with id: ' + country.id + ' saved!';
    });
  }


}



