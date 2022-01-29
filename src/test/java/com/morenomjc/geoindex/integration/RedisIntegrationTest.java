package com.morenomjc.geoindex.integration;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("redis")
@ContextConfiguration(initializers = { ContainerUtils.RedisContainerInitializer.class })
public class RedisIntegrationTest extends ApiIntegrationTest {

}
