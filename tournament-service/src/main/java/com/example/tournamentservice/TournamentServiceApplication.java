package com.example.tournamentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class TournamentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TournamentServiceApplication.class, args);
	}

}
