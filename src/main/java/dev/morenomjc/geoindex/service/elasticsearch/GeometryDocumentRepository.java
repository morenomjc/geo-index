package dev.morenomjc.geoindex.service.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeometryDocumentRepository extends ElasticsearchRepository<GeometryDocument, String> {
}
