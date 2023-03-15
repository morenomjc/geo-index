package dev.morenomjc.geoindex.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GeoIndexRequest {

	private final String key;
	private final String id;
	private final String geoJson;

}
