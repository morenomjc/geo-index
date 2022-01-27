package com.morenomjc.geoindex.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DistanceUnit {

		KM("km"),
		MI("mi");

		private final String abbreviation;
}
