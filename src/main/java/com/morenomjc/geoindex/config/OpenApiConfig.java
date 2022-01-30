package com.morenomjc.geoindex.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("testcontainers")
public class OpenApiConfig {

	@Bean
	public OpenAPI geoIndexApi(@Value("${spring.profiles.active}") String profiles) {
		return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info().title("GeoIndex API Definition")
				.description("Implementation: <b>" + profiles.toUpperCase() + "</b>"));
	}

}
