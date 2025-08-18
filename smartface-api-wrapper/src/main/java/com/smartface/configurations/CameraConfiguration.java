package com.smartface.configurations;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.smartface.dto.FaceDetectorConfig;
import com.smartface.dto.SpoofDetectorConfig;

import lombok.Data;

@Document(collection = "cameraConfig")
@Data
public class CameraConfiguration {

	@Id
	String configurationType;
//	String name;
//	String source;
//	String enabled;
	
	FaceDetectorConfig faceDetectorConfig;
//	SpoofDetectorConfig spoofDetectorConfig;
}
