package dev.morenomjc.geoindex.service.redis;

import com.google.common.collect.Lists;
import dev.morenomjc.geoindex.api.model.DistanceUnit;
import dev.morenomjc.geoindex.service.GeoIndexRequest;
import dev.morenomjc.geoindex.service.GeoIndexService;
import dev.morenomjc.geoindex.service.GeoRadiusRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.geojson.GeoJSONReader;
import org.locationtech.jts.geom.Geometry;
import org.springframework.context.annotation.Profile;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@Profile("redis")
@RequiredArgsConstructor
public class RedisIndexService implements GeoIndexService {

	private static final String MEMBER_ID_FORMAT = "%s:%d";
	private static final String ID_SEPARATOR = ":";
	private final int PARTITION_SIZE = 500;
	private final RedisOperations<String, String> redisOperations;
	private GeoOperations<String, String> geoOperations;

	@Override
	@SneakyThrows
	public Set<String> index(GeoIndexRequest geoIndexRequest) {
		geoOperations = redisOperations.opsForGeo();

		GeoJSONReader geoJSONReader = new GeoJSONReader(geoIndexRequest.getGeoJson());
		Geometry geometry = (Geometry) geoJSONReader.getFeature().getDefaultGeometryProperty().getValue();
		log.info("Type: {}", geometry.getGeometryType());

		Map<String, Point> pointsMap = buildPointsMap(geoIndexRequest.getId(), geometry);
		log.info("Points: {}", pointsMap.size());
		//partition map to avoid memory limit on request size. Fixes ERR Protocol error: invalid multibulk length
		List<Map<String, Point>> partitionedPointsMap = partitionPointsMap(pointsMap);
		log.info("Points partition: {}", partitionedPointsMap.size());
		partitionedPointsMap.forEach(stringPointMap -> {
			Long ptsAdded = geoOperations.add(geoIndexRequest.getKey(), stringPointMap);
			log.info("\t{} point(s) indexed.", ptsAdded);
		});
		return pointsMap.keySet();
	}

	@Override
	public Set<String> radius(GeoRadiusRequest geoRadius) {
		Point center = new Point(geoRadius.getLat(), geoRadius.getLon());
		Distance distance = new Distance(geoRadius.getDist(), getMetric(geoRadius.getUnit()));
		Circle radius = new Circle(center, distance);
		GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = geoOperations.radius(geoRadius.getKey(), radius);
		Set<String> results = extractFromGeoResults(geoResults);
		log.info("Found {} results within {} {}", results.size(), geoRadius.getDist(), geoRadius.getUnit());
		return results;
	}

	private Metric getMetric(DistanceUnit unit) {
		Optional<org.springframework.data.redis.domain.geo.Metrics> metric = Arrays.stream(Metrics.values())
				.filter(metrics -> metrics.getAbbreviation().equals(unit.getAbbreviation())).findFirst();
		return metric.orElseThrow(() -> {
			throw new IllegalArgumentException("Unsupported distance unit " + unit);
		});
	}

	private Set<String> extractFromGeoResults(GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults) {
		if (geoResults != null) {
			return geoResults.getContent().stream()
					.map(geoLocationGeoResult -> geoLocationGeoResult.getContent().getName()).map(id -> {
						int index = id.lastIndexOf(ID_SEPARATOR);
						return id.substring(0, index);
					}).collect(Collectors.toSet());
		}
		return Collections.emptySet();
	}

	private Map<String, Point> buildPointsMap(String id, Geometry geometry) {
		AtomicInteger atomicInteger = new AtomicInteger(0);
		return Arrays.stream(geometry.getCoordinates())
				.collect(
						Collectors.toMap(
								coordinate -> String.format(MEMBER_ID_FORMAT, id, atomicInteger.getAndIncrement()),
								coordinate -> new Point(coordinate.getX(), coordinate.getY()))
				);
	}

	private List<Map<String, Point>> partitionPointsMap(Map<String, Point> pointsMap) {
		List<Map.Entry<String, Point>> list = pointsMap.entrySet().stream().toList();
		return Lists.partition(list, PARTITION_SIZE).stream()
				.map(entries -> entries.stream()
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
						)).toList();
	}

}
