package com.project.webLoLApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class WebLoLAppApplication extends SpringBootServletInitializer {

	public static final String ApiKey = "API-KEY-HERE";

	public static void main(String[] args) {
		SpringApplication.run(WebLoLAppApplication.class, args);
	}

}
