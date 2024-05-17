package com.example.DayBridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationProperties
public class DayBridgeApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(DayBridgeApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DayBridgeApplication.class, args);
	}


}
