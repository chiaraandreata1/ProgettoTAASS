package com.example.lessonservice.controllers;

import com.example.lessonservice.models.Course;
import com.example.lessonservice.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1")
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/courses")
    public List<Course> list(){
        return courseRepository.findAll();
    }

    @PostMapping(value="/courses/create")
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

    @DeleteMapping("/courses/delete")
    public ResponseEntity<String> deleteAllCourses(){
        System.out.println("Delete all Courses...");

        courseRepository.deleteAll();

        return new ResponseEntity<>("All Courses have been deleted!", HttpStatus.OK);
    }
}
