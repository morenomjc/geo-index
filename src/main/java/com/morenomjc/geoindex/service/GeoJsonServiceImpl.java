package com.morenomjc.geoindex.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeoJsonServiceImpl implements GeoJsonService {

	@Override
	public List<List<Double>> extractCoordinates(Geometry geometry) {
		switch (geometry.getGeometryType()) {
		case "Point":
			return extractFromPoint((Point) geometry);
		case "LineString":
			return extractFromLineString((LineString) geometry);
		case "Polygon":
			return extractFromPolygon((Polygon) geometry);
		default:
			return Collections.emptyList();
		}
	}

	public List<Double> getCoordinates(Coordinate coordinate) {
		return List.of(coordinate.getX(), coordinate.getY());
	}

	public List<List<Double>> extractFromPoint(Point point) {
		return Collections.singletonList(getCoordinates(point.getCoordinate()));
	}

	public List<List<Double>> extractFromLineString(LineString lineString) {
		return Arrays.stream(lineString.getCoordinates()).map(this::getCoordinates).collect(Collectors.toList());
	}

	public List<List<Double>> extractFromPolygon(Polygon polygon) {
		return Arrays.stream(polygon.getCoordinates()).map(this::getCoordinates).collect(Collectors.toList());
	}

}
