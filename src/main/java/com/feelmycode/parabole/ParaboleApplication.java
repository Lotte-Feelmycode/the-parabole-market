package com.feelmycode.parabole;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ParaboleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParaboleApplication.class, args);
	}

}
