# geo-index
Simple geospatial index API using different backends

## Implementations

Standalone application is available per implementation. Each is configured to initialize the required container during startup.
This is good if you don't want to manually run the docker image locally. However, the data will be lost when the application is shutdown.

1. `RedisGeoIndexApplication` - Redis
2. `PostgisGeoIndexApplication` - PostGIS
3. `ElasticsearchGeoIndexApplication` - Elasticsearch

If you prefer to save the state of the data on each application run, the base Spring Application `GeoIndexApplication` can still be used by activating the desired implementation using profiles.

## Features
1. Index a GeoJSON Feature object
2. Search for geometric objects within a certain distance

## Integration Tests

The implementation is tested on the API level regardless of the underlying backend. 
The base test `ApiIntegrationTest` is inherited by different test classes to activate the implementation to be tested.
1. `RedisIntegrationTest` - activating the `redis` profile and starting the Redis container
2. `PostgisIntegrationTest` - activating the `postgis` profile and starting the PostGIS container
3. `ElasticsearchIntegrationTest` - activating the `elasticsearch` profile and starting an Elasticsearch container

## Blog

[Geospatial indexing app with different backends using Spring Boot and Testcontainers](https://www.morenomjc.dev/geospatial-indexing)
