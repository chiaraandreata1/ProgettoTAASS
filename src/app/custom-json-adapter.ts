import {JsonAdapter} from "./utilities/json-interceptor";
import {Injectable} from "@angular/core";

@Injectable()
export class CustomJsonAdapter extends JsonAdapter {

  adapt<T extends object>(obj: T): T {
    // if (obj) {
    //   // @ts-ignore
    //   console.log(obj.keys());
    // }
    // console.log(obj);

    return obj;
  }

}
