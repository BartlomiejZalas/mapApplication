import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';


import {AppComponent} from './app.component';
import {AppRoutingModule} from "./app-routing.module";
import {StartComponent} from "./start/start.component";
import {ContinentsComponent} from "./continents/continents.component";
import {CountriesComponent} from "./countries/countries.component";
import {CitiesComponent} from "./cities/cities.component";
import {ContinentsService} from "./services/continents.service";
import {HttpClientModule} from "@angular/common/http";


@NgModule({
  declarations: [
    AppComponent,
    StartComponent,
    ContinentsComponent,
    CountriesComponent,
    CitiesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [ContinentsService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
