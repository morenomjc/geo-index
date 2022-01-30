package com.morenomjc.geoindex.service.postgis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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

	@Type(type = "jts_geometry")
	@Column(nullable = false)
	private Geometry geometry;

}
