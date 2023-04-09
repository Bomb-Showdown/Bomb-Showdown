package edu.eci.arsw.bombshowdownapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"edu.eci.arsw"})
public class BombShowdownApplication {

	public static void main(String[] args) {
		SpringApplication.run(BombShowdownApplication.class, args);
	}

}
