import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {catchError, tap} from "rxjs/operators";
import {Location} from "../model/location";
import {Continent} from "app/model/continent";
import {Country} from "../model/country";
import {City} from "../model/city";
import 'rxjs/add/observable/throw';

const httpOptions = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

@Injectable()
export class LocationsService {
  private apiUrl = 'http://localhost:8080/';

  constructor(private http: HttpClient) {
  }

  getLocations(type: string, continentId: number = null, countryId: number = null,): Observable<Location[]> {
    let continentParam = continentId == null ? null : 'continentId=' + continentId;
    let countryParam = countryId == null ? null : 'countryId=' + countryId;
    let additionalParams = [continentParam, countryParam].filter(v => v != null).join('&');
    let url = this.apiUrl + type + '?' + additionalParams;

    console.log("Request URL: " + url);

    return this.http.get<Location[]>(url)
      .pipe(
        tap(locations => console.log('Fetched locations:', locations)),
        catchError(this.handleError('get' + type))
      );
  }

  removeLocation(locationType: string, locationId: number) {
    const url = this.apiUrl + locationType + '/' + locationId;
    return this.http.delete<Location>(url).pipe(
      tap(_ => console.log(`Deleted location id=${locationId}`)),
      catchError(this.handleError('removeLocation'))
    );
  }

  addContinent(name: string) {
    const url = this.apiUrl + 'continents';
    return this.http.post<Continent>(url, new Continent(0, name), httpOptions).pipe(
      tap((continent: Continent) => console.log(`Added continent w/ id=${continent.id}`)),
      catchError(this.handleError('addContinent'))
    )
  }

  addCountry(name: string, continentId: number) {
    const url = this.apiUrl + 'countries?continentId=' + continentId;
    return this.http.post<Continent>(url, new Country(name), httpOptions).pipe(
      tap((country: Continent) => console.log(`Added country w/ id=${country.id}`)),
      catchError(this.handleError('addCountry'))
    )
  }


  addCity(name: string, countryId: number) {
    const url = this.apiUrl + 'cities?countryId=' + countryId;
    return this.http.post<City>(url, new City(name), httpOptions).pipe(
      tap((city: City) => console.log(`Added city w/ id=${city.id}`)),
      catchError(this.handleError('addCity'))
    )
  }

  private handleError(operation: String) {
    return (err: any) => {
      let errMsg = `error in ${operation}()`;
      console.error(`${errMsg}:`, err);
      if (err instanceof HttpErrorResponse) {
        console.error(`status: ${err.status}, ${err.statusText}`);
        errMsg = err.message;
      }
      return Observable.throw(errMsg);
    }
  }
}
