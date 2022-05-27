import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'forLoop'})
export class ForLoopPipe implements PipeTransform {
  transform(value: number) : any {
    let res = [];
    for (let i = 0; i < value; i++) {
      res.push(i);
    }
    return res;
  }
}


@Pipe({name: 'forTPLoop'})
export class ForTPLoop implements PipeTransform {
  transform(value: number): number[] {
    let res: number[] = [];

    let x = Math.pow(2, Math.ceil(Math.log2(value)));

    for (let i = 0; i < x; i++)
      res.push(i);

    console.log(res)

    return res;
  }
}
