export class Course {
  constructor(
  public id: number,
  public ownerID: number,
  public sporttype: number,
  public instructor: number,
  public levelCourse: string,
  public players: number[],

  public daycourse: string,
  public hourlesson: number,
  public numberweeks: number,
  public priceCourse: number,
  public courtCourse: number,

  public dateCourseCreated: string,
  public firstDayLesson: string,
  public reservationsIDs: number[]
  ) {
  }

  static toJSON(course: Course): object {
    return {
      'id': course.id,
      'ownerID': course.ownerID,
      'sporttype': course.sporttype,
      'instructor': course.instructor,
      'levelCourse': course.levelCourse,
      'players': course.players,
      'daycourse': course.daycourse,
      'hourlesson': course.hourlesson,
      'numberweeks': course.numberweeks,
      'priceCourse': course.priceCourse,
      'courtCourse': course.courtCourse,
      'dateCourseCreated': course.dateCourseCreated,
      'reservationsIDs': course.reservationsIDs
    }
  }
}
