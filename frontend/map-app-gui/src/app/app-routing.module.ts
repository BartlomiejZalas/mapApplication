import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {StartComponent} from "./start/start.component";
import {ContinentsComponent} from "./continents/continents.component";
import {CountriesComponent} from "./countries/countries.component";
import {CitiesComponent} from "./cities/cities.component";
import {ContinentsAddComponent} from "./continents/continents.add.component";

const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'home', component: StartComponent},
  {path: 'continents', component: ContinentsComponent},
  {path: 'continents/add', component: ContinentsAddComponent},
  {path: 'countries', component: CountriesComponent},
  {path: 'cities', component: CitiesComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
