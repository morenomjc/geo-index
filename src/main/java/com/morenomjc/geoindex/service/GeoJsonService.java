package com.morenomjc.geoindex.service;

import com.mapbox.geojson.GeoJson;
import java.util.List;

public interface GeoJsonService {
		List<List<Double>> extractCoordinates(GeoJson geoJson);
}
