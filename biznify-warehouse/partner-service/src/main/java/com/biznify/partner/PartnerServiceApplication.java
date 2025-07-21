package com.biznify.partner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.EnableJms;

@Slf4j
@SpringBootApplication
@EnableJms
public class PartnerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartnerServiceApplication.class, args);
		//log.info("You can call api's port number 8081 for partners-service");
		System.out.println("You can call api's port number 8081 for partners-service");
	}

}
