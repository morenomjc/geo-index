package com.morenomjc.geoindex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeoIndexApplication {

	public static void main(String[] args) {
		createApplication().run(args);
	}

	public static SpringApplication createApplication() {
		return new SpringApplication(GeoIndexApplication.class);
	}

}
