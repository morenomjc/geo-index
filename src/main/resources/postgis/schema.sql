CREATE TABLE geometries
(
    id         BIGINT       NOT NULL,
    geometry   geometry     NOT NULL,
    identifier varchar(255) NOT NULL,
    "key"      varchar(255) NOT NULL,
    CONSTRAINT geometries_pk PRIMARY KEY (id),
    CONSTRAINT geometries_uk UNIQUE (identifier, key)
);
-- This will allow for faster queries
CREATE INDEX geometries_idx
    ON geometries
    USING GIST (geometry);