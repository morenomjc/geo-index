package dev.morenomjc.geoindex.service;

import java.util.Set;

public interface GeoIndexService {

	Set<String> index(GeoIndexRequest geoIndex);

	Set<String> radius(GeoRadiusRequest geoRadius);

}
