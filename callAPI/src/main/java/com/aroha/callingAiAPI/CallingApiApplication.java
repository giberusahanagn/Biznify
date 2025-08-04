package com.aroha.callingAiAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
	
@SpringBootApplication
@ComponentScan("com.aroha.callingAiAPI")
public class CallingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CallingApiApplication.class, args);
	}

}
