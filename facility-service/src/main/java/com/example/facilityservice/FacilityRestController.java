package com.example.facilityservice;

import com.example.facilityservice.models.Court;
import com.example.facilityservice.models.Facility;
import com.example.facilityservice.models.Sport;
import com.example.facilityservice.repositories.CourtRepository;
import com.example.facilityservice.repositories.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class FacilityRestController {

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private Facility facility;

    @GetMapping("init")
    public void init() {
        List<String> levels = Arrays.asList("Newbie", "Experienced", "Expert", "Pro");

        Sport tennis = new Sport();
        tennis.setName("Tennis");
        tennis.setLevels(levels);

        Sport singleTennis = new Sport();
        singleTennis.setName("Single");
        singleTennis.setPlayersPerTeam(1);
        singleTennis.setParent(tennis);

        Sport doubleTennis = new Sport();
        doubleTennis.setName("Double");
        doubleTennis.setPlayersPerTeam(2);
        doubleTennis.setParent(tennis);

        Sport padel = new Sport();
        padel.setName("Padel");
        padel.setPlayersPerTeam(2);
        padel.setLevels(levels);

        sportRepository.saveAll(Arrays.asList(tennis, singleTennis, doubleTennis, padel));

        ArrayList<Court> courts = new ArrayList<>();
        for (Sport sport : new Sport[]{tennis, padel}) {
            for (int j = 0; j < 3; j++) {
                Court court = new Court();
                court.setSport(sport);
                court.setStatus(Court.Status.OPEN);
                courts.add(court);
            }
        }

        courtRepository.saveAll(courts);
    }

    @GetMapping()
    public String info() {
        return "Working";
    }

    @GetMapping("opening-hours")
    public Integer[] getOpeningHours() {
        return new Integer[]{facility.getOpeningTime(), facility.getClosingTime()};
    }

    @GetMapping("hours")
    public Map<String, Integer> getHours() {
        return facility.getHours();
    }

    @GetMapping("sports")
    public List<Sport> getSports() {
        return sportRepository.findAll();
    }

    @GetMapping("sport")
    public Sport getSport(@RequestParam Long id) {
        Optional<Sport> optionalSport = sportRepository.findById(id);
        if (!optionalSport.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return optionalSport.get();
    }

    @GetMapping(value = "courts", params = {})
    public List<Court> getCourts() {
        return courtRepository.findAll();
    }

    @GetMapping(value = "courts", params = {"sport"})
    public List<Court> getCourts(@RequestParam Long sport) {
        return courtRepository.getCourtBySport_Id(sport);
    }
}
