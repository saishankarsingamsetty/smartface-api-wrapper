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

//	@Override
//	public void run(String... args) throws Exception {
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
//
//	}
	
	@Override
	public void run(String... args) {
	    if (repository.count() == 0) {
	        ConfigurationEntity.RangeConfig minFaceSize =
	                new ConfigurationEntity.RangeConfig(Arrays.asList(25, 500), 35, 30);
	        ConfigurationEntity.RangeConfig maxFaceSize =
	                new ConfigurationEntity.RangeConfig(Arrays.asList(500, 600), 600, 600);
	        ConfigurationEntity.RangeConfig maxFaces =
	                new ConfigurationEntity.RangeConfig(Arrays.asList(1, 20), 20, 10);
	        ConfigurationEntity.RangeConfig confidenceThreshold =
	                new ConfigurationEntity.RangeConfig(Arrays.asList(0, 10000), 450, 3000);

	        ConfigurationEntity.FaceDetectorConfig faceDetectorConfig =
	                new ConfigurationEntity.FaceDetectorConfig(minFaceSize, maxFaceSize, maxFaces, confidenceThreshold);

	        ConfigurationEntity.MatchingThreshold matchingThresold =
	                new ConfigurationEntity.MatchingThreshold(Arrays.asList(0, 100), 100, 100);

	        ConfigurationEntity.RangeConfig distantThreshold =
	                new ConfigurationEntity.RangeConfig(Arrays.asList(0, 100), 90, 90);
	        ConfigurationEntity.RangeConfig nearbyThreshold =
	                new ConfigurationEntity.RangeConfig(Arrays.asList(0, 100), 90, 90);

	        ConfigurationEntity.SpoofDetectorConfig spoofDetectorConfig =
	                new ConfigurationEntity.SpoofDetectorConfig(distantThreshold, nearbyThreshold, "default", "default");

	        ConfigurationEntity.ResourceConfig faceDetectorResourceId =
	                new ConfigurationEntity.ResourceConfig(
	                        Arrays.asList("none", "cpu", "gpu"), "cpu", "cpu");

	        ConfigurationEntity.ResourceConfig spoofDetectorResourceIds =
	                new ConfigurationEntity.ResourceConfig(
	                        Arrays.asList("none", "liveness_distant_cpu_remote"), "none", "none");

	        ConfigurationEntity.ConfigItem configItem1 =
	                new ConfigurationEntity.ConfigItem("dummy1", faceDetectorConfig, matchingThresold,
	                        spoofDetectorConfig, faceDetectorResourceId, spoofDetectorResourceIds,
	                        "cpu", "cpu");

	        ConfigurationEntity configEntity =
	                new ConfigurationEntity("dummyConfig", Arrays.asList(configItem1));

	        repository.save(configEntity);

	        System.out.println("✅ Inserted dummy configuration into MongoDB.");
	    }
	}


}
