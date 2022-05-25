package com.example.lessonservice.controllers;

import com.example.lessonservice.models.Course;
import com.example.lessonservice.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/courses")
    public List<Course> list(){
        return courseRepository.findAll();
    }

    @PostMapping("/courses/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody Course course){
        courseRepository.save(course);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<String> deleteCourses(@PathVariable("id") long id){
        System.out.println("Delete course with id = " + id + "...");

        courseRepository.deleteById(id);

        return new ResponseEntity<>("Course deleted!", HttpStatus.OK);
    }

    @GetMapping("/courses/year/{year}/ispending/{ispending}")
    public List<Course> findCompleteCoursesByYear(@PathVariable Integer year, @PathVariable Boolean ispending){
        SimpleDateFormat DAY_TIME_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        StringBuilder startDateTime = new StringBuilder(); StringBuilder endDateTime = new StringBuilder();
        startDateTime.append("01-01-").append(year.toString()); endDateTime.append("31-12-").append(year);
        /*
        try {
            return courseRepository.findAllByEndDateRegistrationBetweenAndPlayersIsLessThanEqual(DAY_TIME_DATE_FORMAT.parse(startDateTime.toString()), DAY_TIME_DATE_FORMAT.parse(endDateTime.toString()), 3);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
         */
        try {
            List<Course> allCoursesByYear = courseRepository.findAllByEndDateRegistrationBetween(DAY_TIME_DATE_FORMAT.parse(startDateTime.toString()), DAY_TIME_DATE_FORMAT.parse(endDateTime.toString()));
            List<Course> allCourses = new ArrayList<>();
            for (Course course : allCoursesByYear) {
                if (course.getPlayers().size() == 3 && !ispending)
                    allCourses.add(course);
                else if (course.getPlayers().size() < 3 && ispending)
                    allCourses.add(course);
            }
            return allCourses;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/courses/{id}")
    public Course updateCourse(@PathVariable("id") long id, @RequestBody Course newCourse) {
        System.out.println("Update Course with ID = " + id + "...");

        return courseRepository.findById(id)
               .map( _course -> {
                           _course.setSporttype(newCourse.getSporttype());
                           _course.setInstructor(newCourse.getInstructor());
                           _course.setPlayers(newCourse.getPlayers());
                           _course.setDaycourse(newCourse.getDaycourse());
                           _course.setHourlesson(newCourse.getHourlesson());
                           _course.setNumberweeks(newCourse.getNumberweeks());
                           _course.setPriceCourse(newCourse.getPriceCourse());
                           _course.setCourtCourse(newCourse.getCourtCourse());
                           _course.setEndDateRegistration(newCourse.getEndDateRegistration());
                           return courseRepository.save(_course);
                       })
               .orElseGet(() -> {
                   return courseRepository.save(newCourse);
               });
    }

    @DeleteMapping("/courses/delete")
    public ResponseEntity<String> deleteAllCourses(){
        System.out.println("Delete all Courses...");

        courseRepository.deleteAll();

        return new ResponseEntity<>("All Courses have been deleted!", HttpStatus.OK);
    }
}
