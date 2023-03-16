package dev.morenomjc.geoindex.integration;

import dev.morenomjc.geoindex.GeoIndexApplication;
import dev.morenomjc.geoindex.utils.ContainerUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("elasticsearch")
@ContextConfiguration(classes = { GeoIndexApplication.class },
		initializers = { ContainerUtils.ElasticSearchContainerInitializer.class })
public class ElasticsearchIntegrationTest extends ApiIntegrationTest {

}
