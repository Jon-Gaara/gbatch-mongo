package com.example.gbatchmongo.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author waite
 */
@Configuration
@EnableMongoRepositories(
        basePackages = {"com.example.gbatchmongo.repository"},
        mongoTemplateRef = "mongodbTemplate")
public class MongoConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("spring.data.mongodb")
    public MongoProperties mongoProperties() {
        return new MongoProperties();
    }

    @Primary
    @Bean(name = "mongodbTemplate")
    public MongoTemplate mongoTemplate(MappingMongoConverter mappingMongoConverter) throws Exception {
        return new MongoTemplate(coreFactory(mongoProperties()), mappingMongoConverter);
    }

    @Bean
    @Primary
    public MongoDatabaseFactory coreFactory(MongoProperties mongoProperties) {
        ConnectionString connectionString = new ConnectionString(mongoProperties.getUri());
        return new SimpleMongoClientDatabaseFactory(MongoClients.create(connectionString),connectionString.getDatabase());
    }

    @Bean
    public MappingMongoConverter mongoConverter(MongoDatabaseFactory mongoDatabaseFactory, MongoMappingContext mongoMappingContext) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        mappingMongoConverter.setMapKeyDotReplacement(".");
        return mappingMongoConverter;
    }
}
