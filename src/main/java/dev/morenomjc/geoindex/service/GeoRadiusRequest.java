package dev.morenomjc.geoindex.service;

import dev.morenomjc.geoindex.api.model.DistanceUnit;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GeoRadiusRequest {

	private final String key;
	private final Double lat;
	private final Double lon;
	private final Double dist;
	private final DistanceUnit unit;

}
