package com.quintor.api.mongoConnection;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<Mt940, String> {
}
