package com.smartface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SmartfaceApiWrapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartfaceApiWrapperApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplae() {
		return new RestTemplate();
	}

}
