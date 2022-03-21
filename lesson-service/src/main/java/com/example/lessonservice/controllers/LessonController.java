package com.example.lessonservice.controllers;

import com.example.lessonservice.models.Lesson;
import com.example.lessonservice.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LessonController {

    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping("/")
    public List<Lesson> list(){
        return lessonRepository.findAll();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody Lesson lesson){
        lessonRepository.save(lesson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLessons(@PathVariable("id") long id){
        System.out.println("Delete lesson with id = " + id + "...");

        lessonRepository.deleteById(id);

        return new ResponseEntity<>("Lesson deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAllLessons(){
        System.out.println("Delete all Lessons...");

        lessonRepository.deleteAll();

        return new ResponseEntity<>("All lessons have been deleted!", HttpStatus.OK);
    }
}
