package com.example.eda.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "edd";
    }

    @Override
    @Bean
    public MongoClientSettings mongoClientSettings() {
        ConnectionString connectionString = new ConnectionString("mongodb://mongo1:27017,mongo2:27018/?replicaSet=my-mongo-set");

        return MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .readPreference(ReadPreference.secondaryPreferred())
                .build();
    }
}
