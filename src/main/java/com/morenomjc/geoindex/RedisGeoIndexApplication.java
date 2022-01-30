package com.morenomjc.geoindex;

import com.morenomjc.geoindex.utils.ContainerUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisGeoIndexApplication {

	public static void main(String[] args) {
		var application = GeoIndexApplication.createApplication();
		application.setAdditionalProfiles("redis");
		application.addInitializers(new ContainerUtils.RedisContainerInitializer());
		application.run(args);
	}

}
