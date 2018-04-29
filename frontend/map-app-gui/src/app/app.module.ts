import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';


import {AppComponent} from './app.component';
import {AppRoutingModule} from "./app-routing.module";
import {StartComponent} from "./start/start.component";
import {ContinentsComponent} from "./continents/continents.component";
import {CountriesComponent} from "./countries/countries.component";
import {CitiesComponent} from "./cities/cities.component";
import {HttpClientModule} from "@angular/common/http";
import {LocationTableComponent} from "./elements/location-table/location-table.component";
import {LocationsService} from "./services/locations.service";
import {ContinentsAddComponent} from "./continents/continents.add.component";
import {FormsModule} from "@angular/forms";
import {CountriesAddComponent} from "./countries/countries.add.component";


@NgModule({
  declarations: [
    AppComponent,
    StartComponent,
    ContinentsComponent,
    CountriesComponent,
    CitiesComponent,
    LocationTableComponent,
    ContinentsAddComponent,
    CountriesAddComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [LocationsService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
