package com.sz.znaczki;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EZnaczkiApplication {
	static final Logger LOGGER = LogManager.getLogger(EZnaczkiApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(EZnaczkiApplication.class, args);
		LOGGER.info("koniec");
	}
}
