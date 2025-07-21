package com.biznify.warehouse;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
@SpringBootApplication
@Slf4j
public class WarehouseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarehouseServiceApplication.class, args);
		
		log.info("warehouse-service started - 8083");
		
		
	}

}
