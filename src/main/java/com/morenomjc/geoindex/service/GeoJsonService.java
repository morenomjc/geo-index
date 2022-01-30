package com.morenomjc.geoindex.service;

import org.locationtech.jts.geom.Geometry;

import java.util.List;

public interface GeoJsonService {

	List<List<Double>> extractCoordinates(Geometry geometry);

}
