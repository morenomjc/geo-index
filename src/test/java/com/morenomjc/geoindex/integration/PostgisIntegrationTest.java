package com.morenomjc.geoindex.integration;

import com.morenomjc.geoindex.GeoIndexApplication;
import com.morenomjc.geoindex.utils.ContainerUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("postgis")
@ContextConfiguration(classes = { GeoIndexApplication.class },
		initializers = { ContainerUtils.PostgisContainerInitializer.class })
public class PostgisIntegrationTest extends ApiIntegrationTest {

}
