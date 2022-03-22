package com.example.reservationservice.controllers;

import com.example.reservationservice.models.Court;
import com.example.reservationservice.repositories.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CourtController {

    @Autowired
    private CourtRepository courtRepository;

    @GetMapping("/courts")
    public List<Court> list() {
        return courtRepository.findAll();
    }

    @PostMapping("/courts/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody Court court) {
        courtRepository.save(court);
    }

    @DeleteMapping("/courts/{id}")
    public ResponseEntity<String> deleteCourts(@PathVariable("id") long id) {
        System.out.println("Delete court with id = " + id + "...");

        courtRepository.deleteById(id);

        return new ResponseEntity<>("Court deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/courts/delete")
    public ResponseEntity<String> deleteAllCourts() {
        System.out.println("Delete all courts...");

        courtRepository.deleteAll();

        return new ResponseEntity<>("All courts have been deleted!", HttpStatus.OK);
    }
}
