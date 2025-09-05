package com.smartface.configurations;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartface.entities.ConfigurationEntity;
import com.smartface.repository.CameraConfigurationRepository;
import com.smartface.repository.ConfigurationRepository;

//@Component
public class CameraConfigurationRunner implements CommandLineRunner {
	
	@Autowired
	CameraConfigurationRepository repo;
	
	@Autowired
	ConfigurationRepository repository;

	@Override
	public void run(String... args) throws Exception {
//		CameraConfiguration securityConfigurations = new CameraConfiguration();
//		securityConfigurations.setConfigurationType("High Security facility");
//		FaceDetectorConfig faceConfig = new FaceDetectorConfig();
//		faceConfig.setMinFaceSize(60);
//		faceConfig.setMaxFaceSize(600);
//		faceConfig.setConfidenceThreshold(5000);
//		
//		securityConfigurations.setFaceDetectorConfig(faceConfig);
//		
//		
//		repo.save(securityConfigurations);

	}
	
	


}
