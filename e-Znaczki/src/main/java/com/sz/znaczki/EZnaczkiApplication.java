package com.sz.znaczki;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EZnaczkiApplication {
	static final Logger LOGGER = LogManager.getLogger(EZnaczkiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EZnaczkiApplication.class, args);
		LOGGER.info("koniec");
	}

	@Bean
	@Profile("prod")
	public RestTemplate createRestTemplate() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
				HttpClientBuilder.create().build());
		return new RestTemplate(clientHttpRequestFactory);
	}
}
