package dev.morenomjc.geoindex.service.elasticsearch;

import dev.morenomjc.geoindex.service.GeoIndexRequest;
import dev.morenomjc.geoindex.service.GeoIndexService;
import dev.morenomjc.geoindex.service.GeoRadiusRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.geojson.GeoJSONReader;
import org.locationtech.jts.geom.Geometry;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Profile("elasticsearch")
@RequiredArgsConstructor
public class ElasticsearchGeoIndexService implements GeoIndexService {

    private final GeometryDocumentRepository geometryDocumentRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private GeoJSONReader geoJSONReader;

    @SneakyThrows
    @Override
    public Set<String> index(GeoIndexRequest geoIndex) {
        geoJSONReader = new GeoJSONReader(geoIndex.getGeoJson());
        Geometry geometry = (Geometry) geoJSONReader.getFeature().getDefaultGeometryProperty().getValue();
        log.info("Type: {}", geometry.getGeometryType());

        geometryDocumentRepository.save(convert(geoIndex.getId(), geometry.toText()));

        return Set.of(geoIndex.getId());
    }

    @Override
    public Set<String> radius(GeoRadiusRequest geoRadius) {
        CriteriaQuery query = new CriteriaQuery(new Criteria("location")
                .within(
                        new Point(geoRadius.getLat(), geoRadius.getLon()),
                        new Distance(geoRadius.getDist()/1000, Metrics.KILOMETERS)
                )
        );
        SearchHits<GeometryDocument> geometryDocumentSearchHits = elasticsearchTemplate
                .search(query, GeometryDocument.class, IndexCoordinates.of("geometries"));
        log.info("Found in ES: {}", geometryDocumentSearchHits.getTotalHits());
        return geometryDocumentSearchHits.stream()
                .map(geometryDocumentSearchHit -> geometryDocumentSearchHit.getContent().getId())
                .collect(Collectors.toSet());
    }

    private GeometryDocument convert(String id, String wkt) {
        return GeometryDocument.builder()
                .id(id)
                .location(wkt)
                .build();
    }
}
