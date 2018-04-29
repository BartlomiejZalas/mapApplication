import {Component, Input, OnInit} from '@angular/core';
import {LocationsService} from "../../services/locations.service";
import {Location} from "../../model/location";
import {ExtraColumn} from "../../model/extracolumn";
import {Country} from "../../model/country";
import {Continent} from "../../model/continent";

@Component({
  selector: 'location-table',
  templateUrl: './location-table.component.html',
})
export class LocationTableComponent implements OnInit {

  @Input() locationType: string;
  @Input() extraColumns: ExtraColumn[];
  @Input() filterByContinent: boolean = false;
  @Input() filterByCountry: boolean = false;

  locations: Location[];
  loading = true;
  continents: Continent[];
  countries: Country[];

  filterContinentValue:number = null;
  filterCountryValue:number = null;

  constructor(private locationService: LocationsService) {
  }

  ngOnInit(): void {
    this.getLocations();
    this.locationService.getLocations('continents').subscribe(c => {
      this.continents = c;
      this.locationService.getLocations('countries').subscribe(c => {
        this.countries = c;
        this.loading = false;
      });
    });
  }

  private getLocations() {
    this.loading = true;
    this.locationService.getLocations(this.locationType, this.filterContinentValue, this.filterCountryValue).subscribe(locations => {
      this.locations = locations;
      this.loading = false;
    });
  }

  updateContinentFilter(continentId: string) {
    this.filterContinentValue = continentId == '' ? null : Number(continentId);
    this.getLocations();
  }

  updateCountryFilter(countryId: string) {
    this.filterCountryValue = countryId == '' ? null : Number(countryId);
    this.getLocations();
  }

  remove(location: Location): void {
    this.loading = true;
    this.locationService.removeLocation(this.locationType, location.id).subscribe(locations => {
      this.getLocations()
    });

  }
}
