package com.morenomjc.geoindex.api.v1;

import com.morenomjc.geoindex.api.model.DistanceUnit;
import com.morenomjc.geoindex.service.GeoIndex;
import com.morenomjc.geoindex.service.GeoIndexService;
import com.morenomjc.geoindex.service.GeoRadius;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Geo Index")
@RestController
@RequestMapping("/v1/geo-indexes")
@RequiredArgsConstructor
public class GeoIndexController {

	private final GeoIndexService geoIndexService;

	@Operation(summary = "Index the GeoJSON object. Returns the list of generated ids for each points indexed.")
	@PostMapping(path = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<String>> index(@PathVariable String key, @RequestParam String id,
			@RequestBody String json) {
		log.info("Indexing request received.");
		return ResponseEntity.ok(geoIndexService.index(new GeoIndex(key, id, json)));
	}

	@Operation(summary = "Search for locations nearby. Returns the ids within radius of input.")
	@GetMapping(path = "/{key}/radius", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<String>> radius(@PathVariable String key, @RequestParam Double lat,
			@RequestParam Double lon, @RequestParam Double dist, @RequestParam DistanceUnit unit) {
		log.info("Radius search request received.");
		return ResponseEntity.ok(geoIndexService.radius(new GeoRadius(key, lat, lon, dist, unit)));
	}

}
