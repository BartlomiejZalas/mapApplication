import {Component, OnInit} from '@angular/core';
import {ContinentsService} from "../services/continents.service";

@Component({
  selector: 'continents',
  templateUrl: './continents.component.html',
})
export class ContinentsComponent implements OnInit{

  continents: Continent[];

  constructor(private continentsService: ContinentsService) { }

  ngOnInit(): void {
    this.getContinents();
  }

  private getContinents() {
    this.continentsService.getContinents().subscribe(heroes => this.continents = heroes);
  }
}
