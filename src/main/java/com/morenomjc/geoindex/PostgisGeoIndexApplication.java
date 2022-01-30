package com.morenomjc.geoindex;

import com.morenomjc.geoindex.utils.ContainerUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PostgisGeoIndexApplication {

	public static void main(String[] args) {
		var application = GeoIndexApplication.createApplication();
		application.setAdditionalProfiles("postgis");
		application.addInitializers(new ContainerUtils.PostgisContainerInitializer());
		application.run(args);
	}

}
