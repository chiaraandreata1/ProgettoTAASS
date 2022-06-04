package com.example.tournamentservice.repositories;

import com.example.tournamentservice.models.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    List<Tournament> findAllByOwnerIs(Long owner);
}
