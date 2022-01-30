# geo-index
Simple geospatial index API using different backends

## Implementations

Standalone application is available per implementation. Each is configured to initialize the required container during startup.
This is good if you don't want to manually run the docker image locally. However, the data will be lost when the application is shutdown.

1. `RedisGeoIndexApplication` - Redis
2. `PostgisGeoIndexApplication` - PostGIS

If you prefer to save the state of the data on each application run, the base Spring Application `GeoIndexApplication` can still be used by activating the desired implementation using profiles.

## Features
1. Index a GeoJSON object
2. Search for geometric objects within a certain distance

### Using Redis
When indexing, all points from the GeoJSON object are extracted and indexed individually.

Each point are indexed using the format `<id>:<index>` within the `key` partition.

Proximity query can only be performed within the `key` partition they are indexed. Returns only the unique ids.

#### Local container

### Using PostGIS

> TODO - support different unit of distance. currently, in miles.

## Integration Tests

The implementation is tested on the API level regardless of the underlying backend. 
The base test `ApiIntegrationTest` is inherited by different test classes to activate the implementation to be tested.
1. `RedisIntegrationTest` - activating the `redis` profile and starting the Redis container
2. `PostgisIntegrationTest` - activating the `postgis` profile and starting the PostGIS container