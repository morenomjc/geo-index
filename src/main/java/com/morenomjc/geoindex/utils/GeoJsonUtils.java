package com.morenomjc.geoindex.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.GeometryAdapterFactory;
import com.mapbox.geojson.GeometryCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.geojson.exception.GeoJsonException;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import java.lang.reflect.Type;
import java.util.Objects;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GeoJsonUtils {

	private static Gson gson() {
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
		gson.registerTypeAdapterFactory(GeometryAdapterFactory.create());
		return gson.create();
	}

	private static String getGeoJsonType(String json) {
		JsonObject jsonObject = gson().fromJson(json, JsonObject.class);
		if (Objects.isNull(jsonObject) || Objects.isNull(jsonObject.get("type"))) {
			throw new GeoJsonException("Cannot parse json");
		}
		String typeValue = jsonObject.get("type").getAsString();
		return Objects.requireNonNullElseGet(typeValue, () -> {
			throw new GeoJsonException("Cannot determine GeoJson type");
		});
	}

	public static <T extends GeoJson> T fromJson(String json) {
		String geoJsonType = getGeoJsonType(json);
		switch (geoJsonType) {
		case "Feature":
			return gson().fromJson(json, (Type) Feature.class);
		case "FeatureCollection":
			return gson().fromJson(json, (Type) FeatureCollection.class);
		case "GeometryCollection":
			return gson().fromJson(json, (Type) GeometryCollection.class);
		case "LineString":
			return gson().fromJson(json, (Type) LineString.class);
		case "MultiLineString":
			return gson().fromJson(json, (Type) MultiLineString.class);
		case "MultiPoint":
			return gson().fromJson(json, (Type) MultiPoint.class);
		case "MultiPolygon":
			return gson().fromJson(json, (Type) MultiPolygon.class);
		case "Polygon":
			return gson().fromJson(json, (Type) Polygon.class);
		case "Point":
			return gson().fromJson(json, (Type) Point.class);
		default:
			throw new GeoJsonException("Invalid GeoJson type");
		}
	}

}
