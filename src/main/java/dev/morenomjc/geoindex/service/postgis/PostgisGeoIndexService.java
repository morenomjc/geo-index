package dev.morenomjc.geoindex.service.postgis;

import dev.morenomjc.geoindex.service.GeoIndexRequest;
import dev.morenomjc.geoindex.service.GeoIndexService;
import dev.morenomjc.geoindex.service.GeoRadiusRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.geojson.GeoJSONReader;
import org.locationtech.jts.geom.Geometry;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Profile("postgis")
@RequiredArgsConstructor
public class PostgisGeoIndexService implements GeoIndexService {

	private final GeometricEntityRepository geometricEntityRepository;
	private GeoJSONReader geoJSONReader;

	@Override
	public Set<String> index(GeoIndexRequest geoIndex) {
		try {
			geoJSONReader = new GeoJSONReader(geoIndex.getGeoJson());
			Geometry geometry = (Geometry) geoJSONReader.getFeature().getDefaultGeometryProperty().getValue();
			log.info("Type: {}", geometry.getGeometryType());

			GeometricEntity entity = geometricEntityRepository
					.save(buildEntity(geoIndex.getKey(), geoIndex.getId(), geometry));
			log.info("Indexed with id: {}", entity.getId());
			return Set.of(geoIndex.getId());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptySet();
	}

	@Override
	public Set<String> radius(GeoRadiusRequest geoRadius) {
		List<GeometricEntity> geometricEntities = geometricEntityRepository.findWithin(geoRadius.getKey(),
				geoRadius.getLat(), geoRadius.getLon(), geoRadius.getDist());
		log.info("Result: {}", geometricEntities.size());
		return geometricEntities.stream().map(GeometricEntity::getIdentifier).collect(Collectors.toSet());
	}

	private GeometricEntity buildEntity(String key, String identifier, Geometry geometry) {
		return GeometricEntity.builder().key(key).identifier(identifier).geometry(geometry).build();
	}

}
