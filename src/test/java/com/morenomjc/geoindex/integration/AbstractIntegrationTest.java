package com.morenomjc.geoindex.integration;

import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Tag;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

@Tag("integration")
@Testcontainers
public abstract class AbstractIntegrationTest {

	public static class ContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

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
