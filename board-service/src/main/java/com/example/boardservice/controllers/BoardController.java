package com.example.boardservice.controllers;

import com.example.boardservice.models.Board;
import com.example.boardservice.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/")
    public List<Board> list(){
        return boardRepository.findAll();
    }

    @GetMapping("/sport/{sport}/type/{type}")
    public List<Board> findBySportAndTypeBoards(@PathVariable String sport, @PathVariable String type) {
        List<Board> boards = boardRepository.findAllBySportAndType(sport, type);
        return boards;
    }

    @GetMapping("/sport/{sport}/type/{type}/date/{date}")
    public List<Board> findBySportAndTypeAndDateBoards(@PathVariable String sport, @PathVariable String type, @PathVariable String date) {
        List<Board> boards = boardRepository.findAllBySportAndTypeAndDate(sport, type, date);
        return boards;
    }

    @GetMapping("/sport/{sport}/owner/{owner}")
    public List<Board> findBySportAndOwnerBoards(@PathVariable String sport, @PathVariable String owner) {
        List<Board> boards = boardRepository.findAllBySportAndOwner(sport, owner);
        return boards;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody Board board){
        boardRepository.save(board);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoards(@PathVariable("id") long id){
        System.out.println("Delete Board with id = " + id + "...");

        boardRepository.deleteById(id);

        return new ResponseEntity<>("Board deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAllBoards(){
        System.out.println("Delete all Boards...");

        boardRepository.deleteAll();

        return new ResponseEntity<>("All Boards have been deleted!", HttpStatus.OK);
    }

    @Autowired
    private HttpSession httpSession;

    @GetMapping("/test")
    public String test() {
        return httpSession.getId();
    }
}
