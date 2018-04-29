import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {catchError, tap} from "rxjs/operators";
import {of} from "rxjs/observable/of";

@Injectable()
export class ContinentsService {
  private continentsUrl = '/api/continents/';

  constructor(private http: HttpClient) { }

  getContinents (): Observable<Continent[]> {
    return this.http.get<Continent[]>(this.continentsUrl)
      .pipe(
        tap(continents => console.log(`fetched continents`)),
        catchError(this.handleError('getContinents', []))
      );
  }

private handleError<T> (operation = 'operation', result?: T) {
  return (error: any): Observable<T> => {
    console.error(error);
    return of(result as T);
  };
}
}
