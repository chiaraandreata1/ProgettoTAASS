package com.example.lessonservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class LessonServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LessonServiceApplication.class, args);
	}

}
