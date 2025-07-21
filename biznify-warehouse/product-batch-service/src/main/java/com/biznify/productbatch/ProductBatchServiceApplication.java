package com.biznify.productbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class ProductBatchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductBatchServiceApplication.class, args);
		System.out.println("ProductBatchServiceApplication 8084");
	}

}
