package com.smartface.entities;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class Camera {

    private String id; // if you have IDs
    private String name;
    private String source;
    private boolean enabled;

    private FaceDetectorConfig faceDetectorConfig;
    private String faceDetectorResourceId;

    private PedestrianDetectorConfig pedestrianDetectorConfig;
    private String pedestrianDetectorResourceId;
    private String pedestrianExtractorResourceId;

    private PalmDetectorConfig palmDetectorConfig;
    private String palmDetectorResourceId;
    private String palmTemplateGeneratorResourceId;

    private String templateGeneratorResourceId;
    private int redetectionTime;
    private int templateGenerationTime;

    private String trackMotionOptimization;
    private String trackSpeedAccuracyMode;

    private String faceSaveStrategy;
    private String pedestrianSaveStrategy;
    private String objectSaveStrategy;

    private String maskImagePath;
    private boolean saveFrameImageData;
    private int imageQuality;

    private boolean mpeG1PreviewEnabled;
    private Integer mpeG1PreviewPort;
    private int mpeG1VideoBitrate;
    private int previewMaxDimension;

    private String serviceName;

    private List<String> spoofDetectorResourceIds;
    private SpoofDetectorConfig spoofDetectorConfig;

    private List<String> palmSpoofDetectorResourceIds;
    private PalmSpoofDetectorConfig palmSpoofDetectorConfig;

    private PreviewAttributesConfig previewAttributesConfig;

    private String objectDetectorResourceId;
    private ObjectDetectorConfig objectDetectorConfig;
    
    @Data
    public static class FaceDetectorConfig {
        private int minFaceSize;
        private int maxFaceSize;
        private int maxFaces;
        private int confidenceThreshold;
    }

    @Data
    public static class PedestrianDetectorConfig {
        private int minPedestrianSize;
        private int maxPedestrianSize;
        private int maxPedestrians;
        private int confidenceThreshold;
    }

    @Data
    public static class PalmDetectorConfig {
        private int minPalmSize;
        private int maxPalmSize;
        private int maxPalms;
        private int confidenceThreshold;
    }

    @Data
    public static class SpoofDetectorConfig {
        private int externalScoreThreshold;
        private int distantLivenessScoreThreshold;
        private int nearbyLivenessScoreThreshold;
        private String distantLivenessConditions;
        private String nearbyLivenessConditions;
    }

    @Data
    public static class PalmSpoofDetectorConfig {
        private int livenessScoreThreshold;
    }

    @Data
    public static class PreviewAttributesConfig {
        private int textFontSize;
        private boolean order;
        private boolean size;
        private boolean quality;
        private boolean yawAngle;
        private boolean pitchAngle;
        private boolean rollAngle;
        private boolean watchlistMemberId;
        private boolean watchlistMemberName;
        private boolean watchlistName;
        private boolean matchingScore;
        private boolean age;
        private boolean gender;
        private boolean templateQuality;
        private boolean faceMaskStatus;
        private boolean faceMaskConfidence;
        private boolean sharpness;
        private boolean brightness;
        private boolean tintedGlasses;
        private boolean heavyFrame;
        private boolean glassStatus;
    }

    @Data
    public static class ObjectDetectorConfig {
        private int minObjectSize;
        private int maxObjectSize;
        private int maxObjects;
        private int confidenceThreshold;
        private ObjectTypesConfiguration objectTypesConfiguration;
    }

    @Data
    public static class ObjectTypesConfiguration {
        private boolean detectCar;
        private boolean detectBus;
        private boolean detectTruck;
        private boolean detectMotorcycle;
        private boolean detectBicycle;
        private boolean detectBoat;
        private boolean detectAirplane;
        private boolean detectTrain;
        private boolean detectBird;
        private boolean detectCat;
        private boolean detectDog;
        private boolean detectHorse;
        private boolean detectSheep;
        private boolean detectCow;
        private boolean detectBear;
        private boolean detectElephant;
        private boolean detectGiraffe;
        private boolean detectZebra;
        private boolean detectSuitcase;
        private boolean detectBackpack;
        private boolean detectHandbag;
        private boolean detectUmbrella;
        private boolean detectKnife;
    }
}
