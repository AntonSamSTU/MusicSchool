package com.NCProject.MusicSchool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.log4j.Logger;

@SpringBootApplication
public class MusicSchoolApplication {

	private static final Logger logger = Logger.getLogger(MusicSchoolApplication.class);


	public static void main(String[] args) {

		logger.info("App started");
		SpringApplication.run(MusicSchoolApplication.class, args);
	}

}
