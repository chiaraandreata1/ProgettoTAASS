import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from "@angular/common/http";
import {map, Observable, tap} from "rxjs";

@Injectable()
export abstract class JsonParser {
  abstract parse(text: string): any;
}


@Injectable()
export abstract class JsonAdapter {
  abstract adapt<T extends object>(obj: T): T;
}


@Injectable()
export class JsonInterceptor implements HttpInterceptor {
  //IL JSON ADAPTER MI DA ERRORE ad esempio su create reservation: no provider for jsonadapter. Togliendo il jsonadapter e riutilizzando il vecchio intercept non da problemi. Probabilmente bisogna inserirlo nel module
  constructor(private jsonParser: JsonParser, private jsonAdapter: JsonAdapter) {}


  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let res: Observable<HttpEvent<any>>;
    req = req.clone({body: this.jsonAdapter.adapt(req.body)});
    console.log(req.body)

    if (req.responseType === 'json') {
      res = this.handleJSONResponse(req, next);
      // res = next.handle(req);
    } else {
      res = next.handle(req);
    }

    return res
  }

/*
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let res: Observable<HttpEvent<any>>;

    //console.log(req.body)

    if (req.responseType === 'json') {
      res = this.handleJSONResponse(req, next);
      // res = next.handle(req);
    } else {
      res = next.handle(req);
    }

    return res
  }
*/

  private handleJSONResponse(httpRequest: HttpRequest<any>, next: HttpHandler) {
    httpRequest = httpRequest.clone({responseType: 'text'});
    return next.handle(httpRequest).pipe(map(value => this.parseJSONResponse(value)));
  }

  private parseJSONResponse(event: HttpEvent<any>) { //non so perchè con course il body è string ma null. La post viene effettuata comunque correttamente
    if (event instanceof HttpResponse && typeof event.body === 'string') {
      return event.clone({body: this.jsonParser.parse(event.body)})
    } else {
      return event;
    }
  }

}
