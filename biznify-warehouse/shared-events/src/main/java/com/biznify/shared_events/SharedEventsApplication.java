package com.biznify.shared_events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SharedEventsApplication {

	public static void main(String[] args) {

		SpringApplication.run(SharedEventsApplication.class, args);

		System.out.println("shared-events");
	}

}
