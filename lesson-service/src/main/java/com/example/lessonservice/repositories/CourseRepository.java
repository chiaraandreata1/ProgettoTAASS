package com.example.lessonservice.repositories;

import com.example.lessonservice.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByEndDateRegistrationBetweenAndSporttype(Date startDateTime, Date endDateTime, Long sporttype);
}
