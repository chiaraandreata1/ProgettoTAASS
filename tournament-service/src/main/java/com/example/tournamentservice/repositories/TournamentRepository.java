package com.example.tournamentservice.repositories;

import com.example.tournamentservice.models.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

}
