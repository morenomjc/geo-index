package com.morenomjc.geoindex.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Profile("postgis")
@EntityScan("com.morenomjc.geoindex.service.postgis")
@EnableJpaRepositories("com.morenomjc.geoindex.service.postgis")
@Configuration
public class PostgisConfig {

}
