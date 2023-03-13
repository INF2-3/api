package com.quintor.api.mongoConnection;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface Mt940Repository extends MongoRepository<Mt940, String> {
    Optional<Mt940> findMt940ById(String id);
}
