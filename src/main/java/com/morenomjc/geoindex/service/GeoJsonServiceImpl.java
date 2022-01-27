package com.morenomjc.geoindex.service;

import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.Point;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeoJsonServiceImpl implements GeoJsonService {

		@Override
		public List<List<Double>> extractCoordinates(GeoJson geoJson) {
				switch (geoJson.type()) {
						case "Point":
								return extractFromPoint((Point) geoJson);
						case "LineString":
						case "Polygon":
						case "MultiPolygon":
						default:
								return Collections.emptyList();
				}
		}

		public List<List<Double>> extractFromPoint(Point point) {
				return Collections.singletonList(point.coordinates());
		}
}
