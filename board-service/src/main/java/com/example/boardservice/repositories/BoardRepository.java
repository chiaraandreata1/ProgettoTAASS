package com.example.boardservice.repositories;

import com.example.boardservice.models.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllBySportAndType(String sport, String type);
    List<Board> findAllBySportAndTypeAndDate(String sport, String type, String date);
    List<Board> findAllBySportAndOwner(String sport, String Owner);
}
