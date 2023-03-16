package dev.morenomjc.geoindex.utils;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
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
			env.setActiveProfiles("redis");
		}

	}

	public static class PostgisContainerInitializer
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		static DockerImageName POSTGIS_IMAGE = DockerImageName.parse("postgis/postgis")
				.asCompatibleSubstituteFor("postgres");
		static PostgreSQLContainer<?> postgis = new PostgreSQLContainer<>(POSTGIS_IMAGE)
				.withInitScript("postgis/schema.sql");

		public static Map<String, Object> getProperties() {
			Startables.deepStart(Stream.of(postgis)).join();

			return Map.of("spring.datasource.url", postgis.getJdbcUrl(), "spring.datasource.username",
					postgis.getUsername(), "spring.datasource.password", postgis.getPassword());
		}

		@Override
		public void initialize(ConfigurableApplicationContext context) {
			var env = context.getEnvironment();
			env.getPropertySources().addFirst(new MapPropertySource("testcontainers", getProperties()));
			env.addActiveProfile("postgis");
		}

	}

	public static class ElasticSearchContainerInitializer
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		static DockerImageName elasticsearchImage = DockerImageName
				.parse("docker.elastic.co/elasticsearch/elasticsearch:8.6.2");

		static ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer(elasticsearchImage)
				.withEnv("discovery.type", "single-node")
				.withEnv("ES_JAVA_OPTS", "-Xms1g -Xmx1g")
				.withEnv("xpack.security.enabled", "false");

		public static Map<String, Object> getProperties() {
			Startables.deepStart(Stream.of(elasticsearchContainer)).join();

			return Map.of("spring.elasticsearch.uris", List.of(elasticsearchContainer.getHttpHostAddress()));
		}

		@Override
		public void initialize(ConfigurableApplicationContext context) {
			var env = context.getEnvironment();
			env.getPropertySources().addFirst(new MapPropertySource("testcontainers", getProperties()));
			env.addActiveProfile("elasticsearch");
		}
	}

}
