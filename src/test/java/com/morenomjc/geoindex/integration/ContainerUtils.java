package com.morenomjc.geoindex.integration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

public abstract class ContainerUtils {

	public static class RedisContainerInitializer
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		static GenericContainer<?> redis = new GenericContainer<>("redis:3-alpine").withExposedPorts(6379);

		public static Map<String, Object> getProperties() {
			Startables.deepStart(Stream.of(redis)).join();

			return Map.of("spring.redis.host", redis.getContainerIpAddress(), "spring.redis.port",
					redis.getFirstMappedPort() + "");
		}

		@Override
		public void initialize(ConfigurableApplicationContext context) {
			var env = context.getEnvironment();
			env.getPropertySources().addFirst(new MapPropertySource("testcontainers", getProperties()));
		}

	}

}
