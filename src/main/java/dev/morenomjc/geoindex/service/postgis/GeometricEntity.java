package dev.morenomjc.geoindex.service.postgis;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.locationtech.jts.geom.Geometry;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "geometries")
public class GeometricEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geometries_seq")
	@SequenceGenerator(name = "geometries_seq", allocationSize = 10)
	private Long id;

	@NaturalId
	@Column(nullable = false)
	private String key;

	@NaturalId
	@Column(nullable = false)
	private String identifier;

	@Column(nullable = false)
	private Geometry geometry;

}
