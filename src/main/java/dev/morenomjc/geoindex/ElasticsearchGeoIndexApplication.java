package dev.morenomjc.geoindex;

import dev.morenomjc.geoindex.utils.ContainerUtils;
import org.springframework.boot.actuate.autoconfigure.data.elasticsearch.ElasticsearchReactiveHealthContributorAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = ElasticsearchReactiveHealthContributorAutoConfiguration.class)
public class ElasticsearchGeoIndexApplication {

	public static void main(String[] args) {
		var application = GeoIndexApplication.createApplication();
		application.setAdditionalProfiles("elasticsearch");
		application.addInitializers(new ContainerUtils.ElasticSearchContainerInitializer());
		application.run(args);
	}

}
