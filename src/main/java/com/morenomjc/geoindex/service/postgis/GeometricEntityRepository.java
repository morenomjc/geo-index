package com.morenomjc.geoindex.service.postgis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GeometricEntityRepository extends JpaRepository<GeometricEntity, Long> {

	// TODO - support different unit of measure for distance
	@Query(value = "SELECT * FROM geometries AS g WHERE g.key = ?1 AND ST_DWithin(g.geometry,ST_MakePoint(?2,?3),?4 * 1609.34)",
			nativeQuery = true)
	List<GeometricEntity> findWithin(String key, Double lat, Double lon, Double dist);

}
