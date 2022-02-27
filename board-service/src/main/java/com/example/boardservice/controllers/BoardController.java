package com.example.boardservice.controllers;

import com.example.boardservice.models.Board;
import com.example.boardservice.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/boards")
    public List<Board> list(){
        return boardRepository.findAll();
    }

    @GetMapping(value = "boards/sport/{sport}/type/{type}")
    public List<Board> findBySportAndTypeBoards(@PathVariable String sport, @PathVariable String type) {
        List<Board> boards = boardRepository.findAllBySportAndType(sport, type);
        return boards;
    }

    @PostMapping(value="/boards/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody Board board){
        boardRepository.save(board);
    }

    @DeleteMapping("/boards/{id}")
    public ResponseEntity<String> deleteBoards(@PathVariable("id") long id){
        System.out.println("Delete Board with id = " + id + "...");

        boardRepository.deleteById(id);

        return new ResponseEntity<>("Board deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/boards/delete")
    public ResponseEntity<String> deleteAllBoards(){
        System.out.println("Delete all Boards...");

        boardRepository.deleteAll();

        return new ResponseEntity<>("All Boards have been deleted!", HttpStatus.OK);
    }
}
