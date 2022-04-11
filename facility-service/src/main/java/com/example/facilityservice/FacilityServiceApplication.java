package com.example.facilityservice;

import com.example.facilityservice.models.Facility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class FacilityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacilityServiceApplication.class, args);
    }
}
