package com.biznify.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication

public class BiznifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiznifyApplication.class, args);
		log.info("Running BiznifyApplication");
		System.out.println("you can call api's ");
	}

}
