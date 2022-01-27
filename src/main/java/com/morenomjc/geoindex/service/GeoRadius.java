package com.morenomjc.geoindex.service;

import com.morenomjc.geoindex.api.model.DistanceUnit;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GeoRadius {

	private final String key;

	private final Double lat;

	private final Double lon;

	private final Double dist;

	private final DistanceUnit unit;

}
