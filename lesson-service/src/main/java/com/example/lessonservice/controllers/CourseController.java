package com.example.lessonservice.controllers;

import com.example.lessonservice.models.Course;
import com.example.lessonservice.rabbithole.FacilityRabbitClient;
import com.example.lessonservice.rabbithole.ReservationRabbitClient;
import com.example.lessonservice.repositories.CourseRepository;
import com.example.shared.models.facility.SportInfo;
import com.example.shared.rabbithole.ReservationOwnerType;
import com.example.shared.rabbithole.ReservationRequest;
import com.example.shared.rabbithole.ReservationResponse;
import com.example.shared.tools.DateSerialization;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ReservationRabbitClient reservationRabbitClient;

    @Autowired
    private FacilityRabbitClient facilityRabbitClient;

    @GetMapping("/")
    public List<Course> list() {
        return courseRepository.findAll();
    }

    Course checkCourse(Course course) {
        /*
        String[] weekday = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
        Calendar cal = Calendar.getInstance(); cal.setTime(course.getFirstDayLesson()); int day = cal.get(Calendar.DAY_OF_WEEK);
        //CONTROLLO TIME
        if (!(course.getFirstDayLesson().getHours()<24 && course.getFirstDayLesson().getHours()>8)
                    || ArrayUtils.indexOf(weekday, course.getDaycourse().toLowerCase())+1!=day)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time variables are not correct");
        */
        //CONTROLLO COURT CORRETTO
        SportInfo sportInfo = facilityRabbitClient.getSportInfo(course.getSporttype());
        List<Long> courtIDs = sportInfo.getCourtIDs();
        if (!courtIDs.contains(course.getCourtCourse()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sport Reservations and Sport Court are not the same");

        return course;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody Course course) {
        try {
            course = checkCourse(course);
            courseRepository.save(course);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourses(@PathVariable("id") long id) {
        System.out.println("Delete course with id = " + id + "...");

        courseRepository.deleteById(id);

        return new ResponseEntity<>("Course deleted!", HttpStatus.OK);
    }

    @GetMapping("sport/{sport}/year/{year}/ispending/{ispending}")
    public List<Course> findCompleteCoursesByYear(@PathVariable long sport, @PathVariable Integer year, @PathVariable Boolean ispending) {
        SimpleDateFormat DAY_TIME_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        StringBuilder startDateTime = new StringBuilder();
        StringBuilder endDateTime = new StringBuilder();
        startDateTime.append("01-01-").append(year.toString());
        endDateTime.append("31-12-").append(year);
        /*
        try {
            return courseRepository.findAllByEndDateRegistrationBetweenAndPlayersIsLessThanEqual(DAY_TIME_DATE_FORMAT.parse(startDateTime.toString()), DAY_TIME_DATE_FORMAT.parse(endDateTime.toString()), 3);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
         */
        try {
            List<Course> allCoursesByYear = courseRepository.findAllByDateCourseCreatedBetweenAndSporttype(DAY_TIME_DATE_FORMAT.parse(startDateTime.toString()), DAY_TIME_DATE_FORMAT.parse(endDateTime.toString()), sport);
            List<Course> allCourses = new ArrayList<>();
            for (Course course : allCoursesByYear) {
                if (course.getPlayers().size() == 3 && !ispending)
                    allCourses.add(course);
                else if (course.getPlayers().size() < 3 && ispending)
                    allCourses.add(course);
            }
            return allCourses;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public Course addPlayer(@PathVariable("id") long id, @RequestBody Course newCourse) {
        System.out.println("Update Course with ID = " + id + "...");
        try {
            return courseRepository.findById(id)
                    .map(course -> {
                        List<Long> oldPlayersList = course.getPlayers();
                        List<Long> newPlayersList = newCourse.getPlayers();
                        if (newPlayersList.size() > 3)
                            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can't add more than 3 players");
                        if (oldPlayersList.size() >= newPlayersList.size())
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New player list has less or same players");
                        for (int i = 0; i < oldPlayersList.size(); i++)
                            if (!oldPlayersList.get(i).equals(newPlayersList.get(i)))
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old player list is not a ordered subset of the new list");
                        course.setPlayers(newCourse.getPlayers());

                        Course finalCourse = courseRepository.save(course);
                        if (finalCourse.getPlayers().size() == 3) {
                            //settiamo il primo giorno di lezioni
                            Date date1 = new Date();
                            date1.setHours(course.getHourlesson());
                            date1.setMinutes(0);
                            String[] weekday = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
                            Calendar cal1 = Calendar.getInstance();
                            cal1.setTime(date1);
                            int day = cal1.get(Calendar.DAY_OF_WEEK);
                            cal1.add(Calendar.DAY_OF_MONTH, weekday.length - day + 1 + ArrayUtils.indexOf(weekday, course.getDaycourse().toLowerCase()));
                            course.setFirstDayLesson(cal1.getTime());

                            List<ReservationRequest> reservationRequests = new ArrayList<>();
                            for (int i = 0; i < finalCourse.getNumberweeks(); i++) {   //la prima reservation course parte 2 settimane dopo, per evitare  di scontrarsi con le reservations private, che hanno una max date di 2 settimane
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(finalCourse.getFirstDayLesson());
                                cal.add(Calendar.WEEK_OF_MONTH, i + 2);
                                Date date = cal.getTime();
                                date.setHours(finalCourse.getHourlesson());
                                ReservationRequest reservationRequest = new ReservationRequest(DateSerialization.serializeDateTime(date), ReservationOwnerType.COURSE, 1, finalCourse.getCourtCourse(), finalCourse.getOwnerID(), course.getSporttype());

                                reservationRequests.add(reservationRequest);
                            }
                            ReservationResponse response = reservationRabbitClient.reserve(reservationRequests);
                            if (response.isDone()) {
                                List<Long> allIDs = new ArrayList<>();
                                for (int i = 0; i < response.getBindings().size(); i++)
                                    allIDs.add(response.getBindings().get(i).getReservationID());
                                finalCourse.setReservationsIDs(allIDs);
                                return finalCourse;
                            } else
                                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "can't update reservation IDs in the completed course");
                        }
                        return finalCourse;
                    })
                    .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find the course")
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAllCourses() {
        System.out.println("Delete all Courses...");

        courseRepository.deleteAll();

        return new ResponseEntity<>("All Courses have been deleted!", HttpStatus.OK);
    }
}
