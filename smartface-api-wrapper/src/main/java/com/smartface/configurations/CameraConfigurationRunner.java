package com.smartface.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartface.dto.FaceDetectorConfig;
import com.smartface.repository.CameraConfigurationRepository;

//@Component
public class CameraConfigurationRunner implements CommandLineRunner {
	
	@Autowired
	CameraConfigurationRepository repo;

	@Override
	public void run(String... args) throws Exception {
		CameraConfiguration securityConfigurations = new CameraConfiguration();
		securityConfigurations.setConfigurationType("High Security facility");
		FaceDetectorConfig faceConfig = new FaceDetectorConfig();
		faceConfig.setMinFaceSize(60);
		faceConfig.setMaxFaceSize(600);
		faceConfig.setConfidenceThreshold(5000);
		
		securityConfigurations.setFaceDetectorConfig(faceConfig);
		
		
		repo.save(securityConfigurations);

	}

}
