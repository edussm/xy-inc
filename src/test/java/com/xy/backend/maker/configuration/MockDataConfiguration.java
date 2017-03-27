package com.xy.backend.maker.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class MockDataConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MockDataConfiguration.class);

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    ResourcePatternResolver resourceResolver;

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean jackson2RepositoryPopulatorFactory() {
        Jackson2RepositoryPopulatorFactoryBean b = new Jackson2RepositoryPopulatorFactoryBean();
        try {
            b.setMapper(jsonMapper);
            b.setResources(resourceResolver.getResources("classpath:mock-data/*.json"));
            b.afterPropertiesSet();
        } catch (Exception e) {
            log.error("Could not load mock data", e);
        }

        return b;
    }
}