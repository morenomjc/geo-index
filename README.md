# geo-index
Simple geospatial index API

## Features
1. Index a GeoJSON
2. Search for ids within a certain distance

### Using Redis
When indexing, all points from the GeoJSON object are extracted and indexed individually.

Each point are indexed using the format `<id>:<index>` within the `key` partition.

Radius query can only be performed within the `key` partition they are indexed. Results only the unique ids.
