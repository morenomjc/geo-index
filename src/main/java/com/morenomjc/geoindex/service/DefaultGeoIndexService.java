package com.morenomjc.geoindex.service;

import java.util.Collections;
import java.util.Set;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("default")
public class DefaultGeoIndexService implements GeoIndexService{

		@Override
		public Set<String> index(GeoIndex geoIndex) {
				return Collections.emptySet();
		}

		@Override
		public Set<String> radius(GeoRadius geoRadius) {
				return Collections.emptySet();
		}
}
