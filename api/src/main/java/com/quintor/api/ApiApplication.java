package com.quintor.api;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
//@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.quintor.api.repository")
@EnableMongoRepositories(basePackages = "com.quintor.api.mongoConnection")
@EntityScan("com.quintor.api.databojects")
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
//    @Bean
//    public CommandLineRunner demo(TransactionRepository transactionRepository) {
//        return (args -> {
//            for(Transaction transaction: transactionRepository.findAll()) {
//                System.out.println(transaction.toString());
//            }
//        });
//    }
}
