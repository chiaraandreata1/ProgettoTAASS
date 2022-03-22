import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from "@angular/common/http";
import {map, Observable, tap} from "rxjs";

@Injectable()
export abstract class JsonParser {
  abstract parse(text: string): any;
}


@Injectable()
export class JsonInterceptor implements HttpInterceptor {

  constructor(private jsonParser: JsonParser) {

  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let res: Observable<HttpEvent<any>>;

    console.log(req.body)

    if (req.responseType === 'json') {
      res = this.handleJSONResponse(req, next);
      // res = next.handle(req);
    } else {
      res = next.handle(req);
    }

    return res
  }

  private handleJSONResponse(httpRequest: HttpRequest<any>, next: HttpHandler) {
    httpRequest = httpRequest.clone({responseType: 'text'});
    return next.handle(httpRequest).pipe(map(value => this.parseJSONResponse(value)));
  }

  private parseJSONResponse(event: HttpEvent<any>) {
    if (event instanceof HttpResponse && typeof event.body === 'string') {
      return event.clone({body: this.jsonParser.parse(event.body)})
    } else {
      return event;
    }
  }

}
