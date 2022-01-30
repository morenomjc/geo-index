package com.morenomjc.geoindex.integration;

import com.morenomjc.geoindex.GeoIndexApplication;
import com.morenomjc.geoindex.utils.ContainerUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("redis")
@ContextConfiguration(classes = { GeoIndexApplication.class },
		initializers = { ContainerUtils.RedisContainerInitializer.class })
public class RedisIntegrationTest extends ApiIntegrationTest {

}
