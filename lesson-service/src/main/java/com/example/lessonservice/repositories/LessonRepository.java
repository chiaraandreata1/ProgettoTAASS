package com.example.lessonservice.repositories;

import com.example.lessonservice.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
