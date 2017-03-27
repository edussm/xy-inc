package com.xy.backend.maker.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;

import cz.jirutka.spring.embedmongo.EmbeddedMongoBuilder;


@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration {

	@Autowired
    private Environment env;
	
	@Override
	protected String getDatabaseName() {
		return env.getProperty("mongo.db.name", "db");
	}

	@Override
	public Mongo mongo() throws Exception {
		return new EmbeddedMongoBuilder()
				.version("3.4.2")
	            .bindIp("127.0.0.1")
	            .port(12345)
	            .build();
	}

}
