package com.example.tournamentservice.repositories;

import com.example.tournamentservice.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchesRepository extends JpaRepository<Match, Long> {
}
