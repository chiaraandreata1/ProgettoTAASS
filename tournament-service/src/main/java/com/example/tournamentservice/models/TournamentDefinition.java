package com.example.tournamentservice.models;

import com.example.shared.models.FacilityHours;
import com.example.shared.models.facility.SportInfo;
import com.example.shared.tools.DateSerialization;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

public class TournamentDefinition {

    private String name;
    private String description;
    private Long sport;
    private Double price;
    private Double prize;

    private Integer courtsCount;
    private Integer maxTeamsNumber;

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

    public Long getSport() {
        return sport;
    }

    public void setSport(Long sport) {
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

    public Integer getCourtsCount() {
        return courtsCount;
    }

    public void setCourtsCount(Integer courtsCount) {
        this.courtsCount = courtsCount;
    }

    public Integer getMaxTeamsNumber() {
        return maxTeamsNumber;
    }

    public void setMaxTeamsNumber(Integer maxTeamsNumber) {
        this.maxTeamsNumber = maxTeamsNumber;
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    public List<TournamentRound> buildRounds() {
        int i;
        int l = maxTeamsNumber;
        int roundI = 0;

        List<TournamentRound> rounds;
        List<Match> round;

        rounds = new ArrayList<>();


        while (l > 1) {
            round = new ArrayList<>();

            for (i = 0; i < l; i += 2)
                round.add(new Match(roundI, i / 2));

            rounds.add(new TournamentRound(round));

            l = round.size();
            roundI++;
        }

        return rounds;
    }

    public int requiredDaysCount(List<TournamentRound> rounds, FacilityHours facilityHours) {

        int matchesPerDay, requiredDays;

        matchesPerDay = Math.floorDiv(facilityHours.getClosingTime() - facilityHours.getOpeningTime(), 3);
        matchesPerDay *= courtsCount;

        requiredDays = 0;

        for (TournamentRound round : rounds) {
            requiredDays += (int) Math.ceil(round.size() * 1. / matchesPerDay);
        }

        return requiredDays;
    }

    public void computeDates(List<TournamentRound> rounds, FacilityHours facilityHours) {

        int openingHour = facilityHours.getOpeningTime(), closingHour = facilityHours.getClosingTime();

        int hoursPerMatch = 3,
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

                    if (time < openingHour)
                        updateCalendar(calendar, dates.remove(0), (time = lastMatchHour));
                }
            }

        }
    }

    private static void updateCalendar(Calendar calendar, int hour) {
        updateCalendar(calendar, null, hour);
    }

    private static void updateCalendar(Calendar calendar, Date day, int hour) {
        if (day != null) {
            calendar.setTime(day);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
    }

}
