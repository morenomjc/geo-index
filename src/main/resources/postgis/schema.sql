CREATE TABLE geometries
(
    id         BIGINT       NOT NULL,
    geometry   geometry     NOT NULL,
    identifier varchar(255) NOT NULL,
    "key"      varchar(255) NOT NULL,
    CONSTRAINT geometries_pkey PRIMARY KEY (id),
    CONSTRAINT uk_2kviec8hw9rqvakku4ahhqbwd UNIQUE (identifier, key)
);