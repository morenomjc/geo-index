package com.morenomjc.geoindex.service;

import java.util.Set;

public interface GeoIndexService {

	Set<String> index(GeoIndex geoIndex);

	Set<String> radius(GeoRadius geoRadius);

}
