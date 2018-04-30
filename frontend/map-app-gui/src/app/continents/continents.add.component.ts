import {Component, OnInit} from '@angular/core';
import {LocationsService} from "../services/locations.service";

@Component({
  selector: 'continents.add',
  templateUrl: './continents.add.component.html'
})
export class ContinentsAddComponent implements OnInit {

  saved: boolean;
  message: string;
  errors: string[] = [];

  constructor(private locationsService: LocationsService) {
  }

  ngOnInit(): void {
  }

  save(continentName: string): void {
    this.errors = [];
    if (continentName == '') {
      this.errors.push("Continent name cannot be empty!");
      return
    }
    this.locationsService.addContinent(continentName).subscribe(
      continent => {this.saved = true; this.message = 'Continent with id: ' + continent.id + ' saved!'},
      err => this.errors.push("Save error: " + err)
      );
  }


}



