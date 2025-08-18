package com.smartface.dto;

import lombok.Data;

@Data
public class SpoofDetectorConfig {

	Integer externalScoreThreshold;
    Integer distantLivenessScoreThreshold;
    Integer nearbyLivenessScoreThreshold;
    String distantLivenessConditions;
    String nearbyLivenessConditions;
}
