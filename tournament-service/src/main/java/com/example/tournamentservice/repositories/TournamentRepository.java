package com.example.tournamentservice.repositories;

import com.example.tournamentservice.models.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    @Query("select t from Tournament t where (t.startingDate >= ?1 or t.endingDate >= ?1) or (t.startingDate <= ?2 or t.endingDate <= ?2)")
    List<Tournament> findAllByDateRange(Date from, Date to);
    @Query("select t from Tournament t where t.status in ?3 and ((t.startingDate >= ?1 or t.endingDate >= ?1) or (t.startingDate <= ?2 or t.endingDate <= ?2))")
    List<Tournament> findAllByDateRangeAndStatus(Date from, Date to, List<Tournament.Status> status);

    List<Tournament> findAllByStatusIn(List<Tournament.Status> statuses);

    @Query("select t from Tournament t where t.startingDate <= ?1 or t.endingDate <= ?1")
    List<Tournament> findAllBeforeDate(Date day);

    @Query("select t from Tournament t where t.startingDate >= ?1 or t.endingDate >= ?1")
    List<Tournament> findAllAfterDate(Date day);

    @Query("select t from Tournament t where t.status in ?2 and (t.startingDate <= ?1 or t.endingDate <= ?1)")
    List<Tournament> findAllBeforeDateAndStatus(Date day, List<Tournament.Status> status);

    @Query("select t from Tournament t where t.status in ?2 and (t.startingDate >= ?1 or t.endingDate >= ?1)")
    List<Tournament> findAllAfterDateAndStatus(Date day, List<Tournament.Status> status);


    @Query(value = "select tournament_id from tournament_teams where player1 = ?1 or player2 = ?1", nativeQuery = true)
    List<Long> findTournamentIdsByPlayer(Long playerID);

    @Query(value = "select t.* from tournament t left join tournament_teams on t.id = tournament_teams.tournament_id where player1 = ?1 or player2 = ?1", nativeQuery = true)
    List<Tournament> findTournamentByPlayer(Long playerID);

    List<Tournament> findAllByIdIn(List<Long> ids);

    List<Tournament> findAllByOwnerIs(Long owner);
}
