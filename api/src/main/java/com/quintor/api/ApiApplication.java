package com.quintor.api;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.naming.ldap.ExtendedRequest;


@SpringBootApplication
//@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.quintor.api.repositories")
@EnableMongoRepositories(basePackages = "com.quintor.api.mongoConnection")
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
