# geo-index
Simple geospatial index API

## Implementation
1. REST API
2. Redis
3. PostGIS
4. MySQL - TODO
5. ElasticSearch - TODO

To enable a chosen implementation, just specify in the active profiles. Chose only 1.

## Features
1. Index a GeoJSON object
2. Search for geometric objects within a certain distance

### Using Redis
When indexing, all points from the GeoJSON object are extracted and indexed individually.

Each point are indexed using the format `<id>:<index>` within the `key` partition.

Proximity query can only be performed within the `key` partition they are indexed. Results only the unique ids.

#### Local container

```shell
docker run -itd --name=redis_local --restart=always -p 6379:6379 -e REDIS_PASSWORD=redispassword redis
```

### Using PostGIS

> TODO - support different unit of distance. currently, in miles.

#### Local container

```shell
docker run -itd --name postgis_local --restart=always -p 5432:5432 -e POSTGRES_PASSWORD=postgis postgis/postgis
```
