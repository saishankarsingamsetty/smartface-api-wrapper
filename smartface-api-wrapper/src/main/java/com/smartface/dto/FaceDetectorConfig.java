package com.smartface.dto;

import lombok.Data;

@Data
public class FaceDetectorConfig {

	Integer minFaceSize;
	Integer maxFaceSize;
	Integer maxFaces;
	Integer confidenceThreshold;
}
