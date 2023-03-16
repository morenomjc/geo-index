package dev.morenomjc.geoindex.service.elasticsearch;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoShapeField;

@Data
@Builder
@Document(indexName = "geometries")
public class GeometryDocument {

    @Id
    private String id;

    @GeoShapeField
    private String location; //WKT format

}
