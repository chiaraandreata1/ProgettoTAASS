import {Serialization} from "../utilities/serialization";

export class Course {
  constructor(
  public id: number,
  public sporttype: string,
  public instructor: number,
  public level: string,
  public players: number[],

  public daycourse: string,
  public hourlesson: number,
  public numberweeks: number,
  public priceCourse: number,
  public courtCourse: number,

  public endDateRegistration: Date,
  public firstDayLesson: Date
  ) {
  }

  static toJSON(course: Course): object {
    return {
      'id': course.id,
      'sporttype': course.sporttype,
      'instructor': course.instructor,
      'level': course.level,
      'players': course.players,
      'daycourse': course.daycourse,
      'hourlesson': course.hourlesson,
      'numberweeks': course.numberweeks,
      'priceCourse': course.priceCourse,
      'courtCourse': course.courtCourse,
      'endDateRegistration': Serialization.serializeDate(course.endDateRegistration),
      'firstDayLesson': Serialization.serializeDateTime(course.firstDayLesson),
    }
  }
}
