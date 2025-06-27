package com.binzify.productBatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.binzify")
public class ProductBatchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductBatchServiceApplication.class, args);
		System.out.println("ProductBatchServiceApplication 8084");
	}

}
