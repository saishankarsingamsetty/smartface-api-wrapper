package com.smartface.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationEntity {

    @Id
    private String name;

    private ConfigItem config;

    // ===== Inner Classes =====

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigItem {
        private String _id;

        private FaceDetectorConfig faceDetectorConfig;
        private MatchingThreshold matchingThresold; // keep typo as in payload
        private SpoofDetectorConfig spoofDetectorConfig;

        @JsonProperty("FaceDetectorResourceId")
        private ResourceConfig faceDetectorResourceId;

        @JsonProperty("SpoofDetectorResourceIds")
        private ResourceConfig spoofDetectorResourceIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RangeConfig {
        private java.util.List<Integer> range;
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
        private java.util.List<Integer> range;
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
        private java.util.List<String> values;
        private String defaultValue;
        private String selectedValue;
    }
}
