package dev.morenomjc.geoindex.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Profile("elasticsearch")
@EnableElasticsearchRepositories("dev.morenomjc.geoindex.service.elasticsearch")
@Configuration
public class ElasticsearchConfig {
}
