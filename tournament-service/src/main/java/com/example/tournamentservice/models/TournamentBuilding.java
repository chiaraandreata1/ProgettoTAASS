package com.example.tournamentservice.models;

import com.example.tournamentservice.tmpservices.FacilityDataProvider;
import com.example.tournamentservice.tmpservices.SportDataProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

public class TournamentBuilding {

    private String name;
    private String description;
    private String sport;
    private Double price;
    private Double prize;
    private String level;
    private List<Team> teams;

    private Integer courtsCount;
    @JsonDeserialize(using = DateSerialization.DateListDeserialize.class)
    private List<Date> dates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrize() {
        return prize;
    }

    public void setPrize(Double prize) {
        this.prize = prize;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Date> getDates() {
        return dates;
    }

    @JsonDeserialize(using = DateSerialization.DateListDeserialize.class)
    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    public Integer getCourtsCount() {
        return courtsCount;
    }

    public void setCourtsCount(Integer courtsCount) {
        this.courtsCount = courtsCount;
    }

    /*
    Utilities
     */

    private void updateCalendar(Calendar calendar, int hour) {
        updateCalendar(calendar, null, hour);
    }

    private void updateCalendar(Calendar calendar, Date day, int hour) {
        if (day != null) {
            calendar.setTime(day);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
    }


    /*

     */

    public void checkRequestValidity() {
        if (sport == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sport required");
        if (teams == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teams list must exists");
        if (dates == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dates list must exists");
    }

    private List<TournamentRound> rounds;

    public void buildRounds() {
        Collections.shuffle(teams);

        int roundI = 0;
        rounds = new ArrayList<>();

        List<Match> round = new ArrayList<>();

        int i;

        for (i = 0; i + 1 < teams.size(); i += 2)
            round.add(new Match(teams.get(i), teams.get(i + 1), roundI, i / 2));
        if (i < teams.size())
            round.add(new Match(teams.get(i), roundI, i / 2));

        rounds.add(new TournamentRound(round));
        roundI++;

        int upperBound = round.size();
        while (upperBound != 1) {
            round = new ArrayList<>();
            for (i = 0; i + 1 < upperBound; i += 2)
                round.add(new Match(roundI, i/2));
            if (i < upperBound)
                round.add(new Match(roundI, i/2));
            upperBound = round.size();
            rounds.add(new TournamentRound(round));
            roundI++;
        }
    }

    public void computeDates() {
        SportDataProvider.SportData sportData = SportDataProvider.getData(getSport());
        FacilityDataProvider.FacilityData facilityData = FacilityDataProvider.FACILITY_DATA;
        int openingHour = facilityData.getOpeningHour(), closingHour = facilityData.getClosingHour();

        if (courtsCount == null)
            courtsCount = sportData.getAvailableCourts();

        int hoursPerMatch = sportData.getHoursPerMatch(),
                lastMatchHour = closingHour - hoursPerMatch,
                matchesForDay = Math.floorDiv(closingHour - openingHour, hoursPerMatch) * courtsCount;

        List<Match> round;
        dates.sort(Collections.reverseOrder());

        for (int i = rounds.size() - 1; i >= 0; i--) {
            round = rounds.get(i).getMatches();

            int daysCount = (int) Math.ceil(1. * round.size() / matchesForDay);

            if (daysCount >  dates.size())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough dates");

            int matchI = 0, court = 0, time = lastMatchHour;

            GregorianCalendar calendar = new GregorianCalendar();

            updateCalendar(calendar, dates.remove(0), lastMatchHour);

            while (matchI < round.size()) {
                Match match = round.get(round.size() - ++matchI);
                match.setCourt(String.valueOf(court++));
                match.setDate(calendar.getTime());

                if (court == courtsCount) {
                    court = 0;
                    time -= hoursPerMatch;
                    updateCalendar(calendar, time);

                    if (time < openingHour) {
                        updateCalendar(calendar, dates.remove(0), (time = lastMatchHour));
                    }
                }
            }

        }
    }

    public Tournament build() {
        checkRequestValidity();
        buildRounds();
        computeDates();
        return new Tournament(
                name,
                description,
                sport,
                price,
                prize,
                level,
                rounds
        );
    }

}
