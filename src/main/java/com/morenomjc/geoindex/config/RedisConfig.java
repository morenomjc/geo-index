package com.morenomjc.geoindex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisOperations;

@Profile("redis")
@Configuration
public class RedisConfig {

	@Bean
	public GeoOperations<String, String> geoOperations(final RedisOperations<String, String> redisOperations) {
		return redisOperations.opsForGeo();
	}

}
