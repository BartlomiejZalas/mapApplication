import {Component, Input, OnInit} from '@angular/core';
import {LocationsService} from "../../services/locations.service";
import {Location} from "../../model/location";

@Component({
  selector: 'location-table',
  templateUrl: './location-table.component.html',
})
export class LocationTableComponent implements OnInit {

  @Input() locationType: string;
  locations: Location[];
  loading = true;

  constructor(private locationService: LocationsService) {
  }

  ngOnInit(): void {
    this.getContinents();
  }

  private getContinents() {
    this.loading = true;
    this.locationService.getLocations(this.locationType).subscribe(locations => {
      this.locations = locations;
      this.loading = false;
    });
  }

  remove(location: Location): void {
    this.loading = true;
    this.locationService.removeLocation(this.locationType, location.id).subscribe(locations => {
      this.getContinents()
    });

  }
}
