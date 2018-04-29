import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {catchError, tap} from "rxjs/operators";
import {of} from "rxjs/observable/of";
import {Location} from "../model/location";
import {Continent} from "app/model/continent";

const httpOptions = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

@Injectable()
export class LocationsService {
  private apiUrl = '/api/';

  constructor(private http: HttpClient) {
  }

  getLocations(type: string): Observable<Location[]> {
    return this.http.get<Location[]>(this.apiUrl + type)
      .pipe(
        tap(locations => console.log('Fetched continents')),
        catchError(this.handleError('get' + type, []))
      );
  }

  removeLocation(locationType: string, locationId: number) {
    const url = this.apiUrl + locationType + '/' + locationId;
    return this.http.delete<Location>(url).pipe(
      tap(_ => console.log(`Deleted location id=${locationId}`)),
      catchError(this.handleError<Location>('removeLocation'))
    );
  }

  addContinent(name: string) {
    const url = this.apiUrl + 'continents';
    return this.http.post<Continent>(url, new Continent(0, name), httpOptions).pipe(
      tap((continent: Continent) => console.log(`Added continent w/ id=${continent.id}`)),
      catchError(this.handleError<Continent>('addContinent'))
    )
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }


}
