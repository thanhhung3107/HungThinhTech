package com.poly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.poly.dao")
@SpringBootApplication
public class AsmJava5Application {

	public static void main(String[] args) {
		SpringApplication.run(AsmJava5Application.class, args);
	}

}
