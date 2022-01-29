package com.morenomjc.geoindex.integration;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("postgis")
@ContextConfiguration(initializers = { ContainerUtils.PostgisContainerInitializer.class })
public class PostgisIntegrationTest extends ApiIntegrationTest {

}
