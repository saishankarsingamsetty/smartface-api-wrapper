package com.smartface.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationEntity {

    @Id
    private String name; // unique camera name or config name

    private FaceDetectorConfig faceDetectorConfig;
    private MatchingThreshold matchingThreshold;
    private SpoofDetectorConfig spoofDetectorConfig;
    private ResourceConfig faceDetectorResource;
    private ResourceConfig spoofDetectorResource;

    // ===== Shared Inner Types =====

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RangeConfig {
        private List<Integer> range;
        private int defaultValue;
        private int selectedValue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FaceDetectorConfig {
        private RangeConfig minFaceSize;
        private RangeConfig maxFaceSize;
        private RangeConfig maxFaces;
        private RangeConfig confidenceThreshold;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchingThreshold {
        private List<Integer> range;
        private int defaultValue;
        private int selectedValue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpoofDetectorConfig {
        private RangeConfig distantLivenessScoreThreshold;
        private RangeConfig nearbyLivenessScoreThreshold;
        private String distantLivenessConditions;
        private String nearbyLivenessConditions;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceConfig {
        private List<String> values;
        private String defaultValue;
        private String selectedValue;
    }
}
