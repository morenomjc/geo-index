package dev.morenomjc.geoindex.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Profile("postgis")
@EntityScan("dev.morenomjc.geoindex.service.postgis")
@EnableJpaRepositories("dev.morenomjc.geoindex.service.postgis")
@Configuration
public class PostgisConfig {

}
