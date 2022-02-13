package com.example.boardservice.repositories;

import com.example.boardservice.models.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
