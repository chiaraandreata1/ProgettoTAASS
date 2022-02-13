package com.example.userservice.controllers;

import com.example.userservice.models.User;
import com.example.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> list(){
        return userRepository.findAll();
    }

    @PostMapping(value="/users/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody User user){
        userRepository.save(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUsers(@PathVariable("id") long id){
        System.out.println("Delete user with id = " + id + "...");

        userRepository.deleteById(id);

        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/users/delete")
    public ResponseEntity<String> deleteAllUsers(){
        System.out.println("Delete all users...");

        userRepository.deleteAll();

        return new ResponseEntity<>("All users have been deleted!", HttpStatus.OK);
    }
}
