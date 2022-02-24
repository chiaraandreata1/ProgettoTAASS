package com.example.lessonservice.repositories;

import com.example.lessonservice.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
