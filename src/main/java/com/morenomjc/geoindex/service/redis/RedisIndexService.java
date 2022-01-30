package com.morenomjc.geoindex.service.redis;

import com.morenomjc.geoindex.api.model.DistanceUnit;
import com.morenomjc.geoindex.service.GeoIndex;
import com.morenomjc.geoindex.service.GeoIndexService;
import com.morenomjc.geoindex.service.GeoJsonService;
import com.morenomjc.geoindex.service.GeoRadius;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.springframework.context.annotation.Profile;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@Profile("redis")
@RequiredArgsConstructor
public class RedisIndexService implements GeoIndexService {

	private static final String ID_FORMAT = "%s:%d";

	private static final String ID_SEPARATOR = ":";

	private final GeoJsonService geoJsonService;

	private final GeometryJSON geometryJSON = new GeometryJSON();

	private final GeoOperations<String, String> geoOperations;

	@Override
	public Set<String> index(GeoIndex geoIndex) {
		try {
			Geometry geometry = geometryJSON.read(geoIndex.getGeoJson());
			log.info("Type: {}", geometry.getGeometryType());

			List<List<Double>> coordinates = geoJsonService.extractCoordinates(geometry);
			return indexCoordinates(geoIndex.getKey(), geoIndex.getId(), coordinates);
		}
		catch (IOException e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
	}

	@Override
	public Set<String> radius(GeoRadius geoRadius) {
		Point center = new Point(geoRadius.getLat(), geoRadius.getLon());
		Distance distance = new Distance(geoRadius.getDist(), getMetric(geoRadius.getUnit()));
		Circle radius = new Circle(center, distance);
		GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = geoOperations.radius(geoRadius.getKey(), radius);
		Set<String> results = extractFromGeoResults(geoResults);
		log.info("Found {} results within {} {}", results.size(), geoRadius.getDist(), geoRadius.getUnit());
		return results;
	}

	private Metric getMetric(DistanceUnit unit) {
		Optional<Metrics> metric = Arrays.stream(Metrics.values())
				.filter(metrics -> metrics.getAbbreviation().equals(unit.getAbbreviation())).findFirst();
		return metric.orElseThrow(() -> {
			throw new IllegalArgumentException("Unsupported distance unit " + unit);
		});
	}

	private Set<String> indexCoordinates(String key, String idParent, List<List<Double>> coordinates) {
		log.info("Indexing {} coordinates", coordinates.size());

		Map<String, Point> geoPoints = new HashMap<>();

		AtomicInteger idCounter = new AtomicInteger(0);
		coordinates.forEach(coordinate -> {
			String id = String.format(ID_FORMAT, idParent, idCounter.getAndIncrement());
			Point point = new Point(coordinate.get(0), coordinate.get(1));
			geoPoints.put(id, point);
		});
		Long result = geoOperations.add(key, geoPoints);
		log.info("Indexed {} points.", result);
		return geoPoints.keySet();
	}

	private Set<String> extractFromGeoResults(GeoResults<GeoLocation<String>> geoResults) {
		if (geoResults != null) {
			return geoResults.getContent().stream()
					.map(geoLocationGeoResult -> geoLocationGeoResult.getContent().getName()).map(id -> {
						int index = id.lastIndexOf(ID_SEPARATOR);
						return id.substring(0, index);
					}).collect(Collectors.toSet());
		}
		return Collections.emptySet();
	}

}
