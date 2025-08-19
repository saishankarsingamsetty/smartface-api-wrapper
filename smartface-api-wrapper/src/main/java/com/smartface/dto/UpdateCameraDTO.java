package com.smartface.dto;

import java.util.List;
import lombok.Data;

@Data
public class UpdateCameraDTO {

    private String id;
    private String name;
    private String source;
    private Boolean enabled; // use Boolean instead of boolean

    private FaceDetectorConfig faceDetectorConfig;
    private PedestrianDetectorConfig pedestrianDetectorConfig;
    private PalmDetectorConfig palmDetectorConfig;

    private String faceDetectorResourceId;
    private String templateGeneratorResourceId;
    private String pedestrianDetectorResourceId;
    private String pedestrianExtractorResourceId;
    private String palmDetectorResourceId;
    private String palmTemplateGeneratorResourceId;

    private Integer redetectionTime;          // Integer instead of int
    private Integer templateGenerationTime;
    private String trackMotionOptimization;
    private String trackSpeedAccuracyMode;

    private String faceSaveStrategy;
    private String pedestrianSaveStrategy;
    private String maskImagePath;
    private Boolean saveFrameImageData;      // Boolean instead of boolean
    private Integer imageQuality;

    private Boolean mpeG1PreviewEnabled;
    private Integer mpeG1PreviewPort;
    private Integer mpeG1VideoBitrate;
    private Integer previewMaxDimension;

    private List<String> spoofDetectorResourceIds;
    private SpoofDetectorConfig spoofDetectorConfig;

    private List<String> palmSpoofDetectorResourceIds;
    private PalmSpoofDetectorConfig palmSpoofDetectorConfig;

    private PreviewAttributesConfig previewAttributesConfig;

    private String objectDetectorResourceId;
    private String objectSaveStrategy;
    private ObjectDetectorConfig objectDetectorConfig;

    @Data
    public static class FaceDetectorConfig {
        private Integer minFaceSize;
        private Integer maxFaceSize;
        private Integer maxFaces;
        private Integer confidenceThreshold;
    }

    @Data
    public static class PedestrianDetectorConfig {
        private Integer minPedestrianSize;
        private Integer maxPedestrianSize;
        private Integer maxPedestrians;
        private Integer confidenceThreshold;
    }

    @Data
    public static class PalmDetectorConfig {
        private Integer minPalmSize;
        private Integer maxPalmSize;
        private Integer maxPalms;
        private Integer confidenceThreshold;
    }

    @Data
    public static class SpoofDetectorConfig {
        private Integer externalScoreThreshold;
        private Integer distantLivenessScoreThreshold;
        private Integer nearbyLivenessScoreThreshold;
        private String distantLivenessConditions;
        private String nearbyLivenessConditions;
    }

    @Data
    public static class PalmSpoofDetectorConfig {
        private Integer livenessScoreThreshold;
    }

    @Data
    public static class PreviewAttributesConfig {
        private Integer textFontSize;
        private Boolean order;
        private Boolean size;
        private Boolean quality;
        private Boolean yawAngle;
        private Boolean pitchAngle;
        private Boolean rollAngle;
        private Boolean watchlistMemberId;
        private Boolean watchlistMemberName;
        private Boolean watchlistName;
        private Boolean matchingScore;
        private Boolean age;
        private Boolean gender;
        private Boolean templateQuality;
        private Boolean faceMaskStatus;
        private Boolean faceMaskConfidence;
        private Boolean sharpness;
        private Boolean brightness;
        private Boolean tintedGlasses;
        private Boolean heavyFrame;
        private Boolean glassStatus;
    }

    @Data
    public static class ObjectDetectorConfig {
        private Integer minObjectSize;
        private Integer maxObjectSize;
        private Integer maxObjects;
        private Integer confidenceThreshold;
        private ObjectTypesConfiguration objectTypesConfiguration;
    }

    @Data
    public static class ObjectTypesConfiguration {
        private Boolean detectCar;
        private Boolean detectBus;
        private Boolean detectTruck;
        private Boolean detectMotorcycle;
        private Boolean detectBicycle;
        private Boolean detectBoat;
        private Boolean detectAirplane;
        private Boolean detectTrain;
        private Boolean detectBird;
        private Boolean detectCat;
        private Boolean detectDog;
        private Boolean detectHorse;
        private Boolean detectSheep;
        private Boolean detectCow;
        private Boolean detectBear;
        private Boolean detectElephant;
        private Boolean detectGiraffe;
        private Boolean detectZebra;
        private Boolean detectSuitcase;
        private Boolean detectBackpack;
        private Boolean detectHandbag;
        private Boolean detectUmbrella;
        private Boolean detectKnife;
    }
}
