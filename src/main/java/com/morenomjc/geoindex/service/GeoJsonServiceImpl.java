package com.morenomjc.geoindex.service;

import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeoJsonServiceImpl implements GeoJsonService {

	@Override
	public List<List<Double>> extractCoordinates(GeoJson geoJson) {
		switch (geoJson.type()) {
		case "Point":
			return extractFromPoint((Point) geoJson);
		case "LineString":
			return extractFromLineString((LineString) geoJson);
		case "Polygon":
			return extractFromPolygon((Polygon) geoJson);
		default:
			return Collections.emptyList();
		}
	}

	public List<Double> toCoordinates(Point point) {
		return point.coordinates();
	}

	public List<List<Double>> extractFromPoint(Point point) {
		return Collections.singletonList(point.coordinates());
	}

	public List<List<Double>> extractFromLineString(LineString lineString) {
		return lineString.coordinates().stream().map(this::toCoordinates).collect(Collectors.toList());
	}

	public List<List<Double>> extractFromPolygon(Polygon polygon) {
		return polygon.coordinates().stream().flatMap(Collection::stream).map(this::toCoordinates)
				.collect(Collectors.toList());
	}

}
