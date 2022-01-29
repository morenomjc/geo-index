package com.morenomjc.geoindex.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("postgis")
@ComponentScan(basePackages = { "com.morenomjc.service.postgis" })
@Configuration
public class PostgisConfig {

}
