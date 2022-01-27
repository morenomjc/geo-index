package com.morenomjc.geoindex.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DistanceUnit {

	KM("km"), M("m"), MI("mi"), FT("ft");

	private final String abbreviation;

}
