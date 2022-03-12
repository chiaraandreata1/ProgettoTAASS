package com.example.tournamentservice.controllers;

import com.example.tournamentservice.models.Tournament;
import com.example.tournamentservice.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@CrossOrigin(origins = "http://localhost:4200")
//@RequestMapping("/api/v1")
public class TournamentController {
//    @Autowired
//    private TournamentRepository tournamentRepository;
//
//    @GetMapping("/tournaments")
//    public List<Tournament> list(){
//        return tournamentRepository.findAll();
//    }
//
//    @PostMapping(value="/tournaments/create")
//    @ResponseStatus(HttpStatus.OK)
//    public void create(@RequestBody Tournament tournament){
//        tournamentRepository.save(tournament);
//    }
//
//    @DeleteMapping("/tournaments/{id}")
//    public ResponseEntity<String> deleteTournaments(@PathVariable("id") long id){
//        System.out.println("Delete tournament with id = " + id + "...");
//
//        tournamentRepository.deleteById(id);
//
//        return new ResponseEntity<>("Tournament deleted!", HttpStatus.OK);
//    }
//
//    @DeleteMapping("/tournaments/delete")
//    public ResponseEntity<String> deleteAllTournaments(){
//        System.out.println("Delete all tournaments...");
//
//        tournamentRepository.deleteAll();
//
//        return new ResponseEntity<>("All tournaments have been deleted!", HttpStatus.OK);
//    }
}
