package com.example.gbatchmongo.configuration;

import com.mongodb.ReadPreference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = {"com.example.gbatchmongo.mongo.primary"},
        mongoTemplateRef = "primaryMongodbTemplate")
public class MongodbPrimaryConfig {

    @Bean(name = "primaryMongodbTemplate")
    public MongoTemplate primaryMongoTemplate(MappingMongoConverter mappingMongoConverter, MongoDatabaseFactory mongoDatabaseFactory) {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory, mappingMongoConverter);
        mongoTemplate.setReadPreference(ReadPreference.primaryPreferred());
        return mongoTemplate;
    }
}