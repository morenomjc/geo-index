package dev.morenomjc.geoindex.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morenomjc.geoindex.api.model.DistanceUnit;
import dev.morenomjc.geoindex.utils.TestDataProvider;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
abstract class ApiIntegrationTest {

	private static final String BASE_URL = "/v1/geo-indexes";
	private static final String GEOJSON_FOLDER = "src/test/resources/geojson/";
	private static final String INDEX_KEY = "test-index";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	static Stream<Arguments> geoJsonFiles() {
		return Stream.of(of("Point", "1", TestDataProvider.readStringFromFile(GEOJSON_FOLDER + "Point.json")),
				of("LineString", "2", TestDataProvider.readStringFromFile(GEOJSON_FOLDER + "LineString.json")),
				of("Polygon", "3", TestDataProvider.readStringFromFile(GEOJSON_FOLDER + "Polygon.json")));
	}

	static Stream<Arguments> searchQueries() {
		return Stream.of(of(4.892692565917969, 52.37203595971475, 37000.0, DistanceUnit.M, Set.of("1", "2")));
	}

	@Order(1)
	@ParameterizedTest(name = "{index} - {0}")
	@MethodSource("geoJsonFiles")
	void testIndexEndpointUsingDifferentGeoJsonTypes(String type, String id, String geoJson) throws Exception {
		performIndexRequest(INDEX_KEY, id, geoJson)
				.andExpectAll(
						status().isOk(),
						jsonPath("$.*").isNotEmpty()
				).andDo(print());
	}

	@Order(2)
	@ParameterizedTest
	@MethodSource("searchQueries")
	void testSearchEndpointUsingDifferentQueries(Double lat, Double lon, Double dist, DistanceUnit unit,
			Set<String> ids) throws Exception {

		MvcResult mvcResult = performSearchRequest(INDEX_KEY, lat, lon, dist, unit)
				.andExpectAll(status().isOk())
				.andDo(print()).andReturn();

		String response = mvcResult.getResponse().getContentAsString();
		Set<String> returnedIds = objectMapper.readValue(response, new TypeReference<>() {});

		assertThat(returnedIds).containsAll(ids);
	}

	private ResultActions performIndexRequest(String key, String identifier, String geoJson) throws Exception {
		return this.mockMvc.perform(post(BASE_URL + "/{0}", key).contentType(MediaType.APPLICATION_JSON)
				.param("id", identifier).content(geoJson));
	}

	private ResultActions performSearchRequest(String key, Double lat, Double lon, Double dist, DistanceUnit unit)
			throws Exception {
		return this.mockMvc.perform(get(BASE_URL + "/{0}/radius", key)
				.contentType(MediaType.APPLICATION_JSON)
				.param("lat", String.valueOf(lat))
				.param("lon", String.valueOf(lon))
				.param("dist", String.valueOf(dist))
				.param("unit", unit.name()));
	}

}
