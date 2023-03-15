package dev.morenomjc.geoindex.service.postgis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GeometricEntityRepository extends JpaRepository<GeometricEntity, Long> {

	@Query(value = "SELECT * FROM geometries AS g WHERE g.key = ?1 AND ST_DWithin(ST_SetSRID(g.geometry, 4326)\\:\\:geography, ST_SetSRID(ST_MakePoint(?2,?3), 4326)\\:\\:geography,?4)",
			nativeQuery = true)
	List<GeometricEntity> findWithin(String key, Double lat, Double lon, Double dist);

}
