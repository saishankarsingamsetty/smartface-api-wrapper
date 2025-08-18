package com.smartface.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartface.configurations.CameraConfiguration;

public interface CameraConfigurationRepository extends MongoRepository<CameraConfiguration, String>{

}
