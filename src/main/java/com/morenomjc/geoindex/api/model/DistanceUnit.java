package com.morenomjc.geoindex.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DistanceUnit {

	M("m");

	private final String abbreviation;

}
