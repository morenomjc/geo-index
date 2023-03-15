package dev.morenomjc.geoindex.integration;

import dev.morenomjc.geoindex.GeoIndexApplication;
import dev.morenomjc.geoindex.utils.ContainerUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("redis")
@ContextConfiguration(classes = { GeoIndexApplication.class },
		initializers = { ContainerUtils.RedisContainerInitializer.class })
public class RedisIntegrationTest extends ApiIntegrationTest {

}
