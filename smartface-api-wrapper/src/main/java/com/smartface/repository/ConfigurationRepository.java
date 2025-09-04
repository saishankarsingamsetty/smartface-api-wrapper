package com.smartface.repository;

import com.smartface.entities.ConfigurationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends MongoRepository<ConfigurationEntity, String> {
}

