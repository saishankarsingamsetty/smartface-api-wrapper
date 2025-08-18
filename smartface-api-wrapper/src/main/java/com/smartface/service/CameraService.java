package com.smartface.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smartface.SmartfaceApiWrapperApplication;
import com.smartface.application.SmartfaceProperties;
import com.smartface.dto.CreateCameraDTO;
import com.smartface.exception.SmartfaceException;

@Service
public class CameraService {

    private final SmartfaceApiWrapperApplication smartfaceApiWrapperApplication;

	@Autowired
	SmartfaceProperties smartfaceProperties;

	@Autowired
	RestTemplate restTemplate;

    CameraService(SmartfaceApiWrapperApplication smartfaceApiWrapperApplication) {
        this.smartfaceApiWrapperApplication = smartfaceApiWrapperApplication;
    }

	public String addCamera(CreateCameraDTO dto) {
		
		String url = smartfaceProperties.getBaseurl()+"Cameras";
		
		String body = """
						{
		  "name": "%s",
		  "source": "%s",
		  "enabled": true,
		  "faceDetectorConfig": {
		    "minFaceSize": 35,
		    "maxFaceSize": 600,
		    "maxFaces": 20,
		    "confidenceThreshold": 450
		  },
		  "faceDetectorResourceId": "cpu",
		  "pedestrianDetectorConfig": {
		    "minPedestrianSize": 80,
		    "maxPedestrianSize": 2000,
		    "maxPedestrians": 20,
		    "confidenceThreshold": 2500
		  },
		  "palmDetectorConfig": {
		    "minPalmSize": 200,
		    "maxPalmSize": 1200,
		    "maxPalms": 1,
		    "confidenceThreshold": 5000
		  },
		  "pedestrianDetectorResourceId": "none",
		  "pedestrianExtractorResourceId": "none",
		  "templateGeneratorResourceId": "cpu",
		  "palmDetectorResourceId": "none",
		  "palmTemplateGeneratorResourceId": "cpu",
		  "redetectionTime": 500,
		  "templateGenerationTime": 500,
		  "trackMotionOptimization": "track.motion_optimization.history_long.fast",
		  "trackSpeedAccuracyMode": "accurate",
		  "faceSaveStrategy": "FirstFace, BestFace",
		  "pedestrianSaveStrategy": "Best",
		  "maskImagePath": null,
		  "saveFrameImageData": true,
		  "imageQuality": 90,
		  "mpeG1PreviewEnabled": true,
		  "mpeG1PreviewPort": null,
		  "mpeG1VideoBitrate": 450000,
		  "previewMaxDimension": 640,
		  "serviceName": "",
		  "spoofDetectorResourceIds": [
		    "none"
		  ],
		  "palmSpoofDetectorResourceIds": [
		    "none"
		  ],
		  "spoofDetectorConfig": {
		    "externalScoreThreshold": 0,
		    "distantLivenessScoreThreshold": 90,
		    "nearbyLivenessScoreThreshold": 90,
		    "distantLivenessConditions": "default",
		    "nearbyLivenessConditions": "default"
		  },
		  "palmSpoofDetectorConfig": {
		    "livenessScoreThreshold": 85
		  },
		  "previewAttributesConfig": {
		    "textFontSize": 12,
		    "order": false,
		    "size": false,
		    "quality": false,
		    "yawAngle": false,
		    "pitchAngle": false,
		    "rollAngle": false,
		    "watchlistMemberId": false,
		    "watchlistMemberName": true,
		    "watchlistName": false,
		    "matchingScore": false,
		    "age": true,
		    "gender": true,
		    "templateQuality": false,
		    "faceMaskStatus": false,
		    "faceMaskConfidence": false,
		    "sharpness": false,
		    "brightness": false,
		    "tintedGlasses": false,
		    "heavyFrame": false,
		    "glassStatus": false
		  },
		  "objectDetectorResourceId": "none",
		  "objectSaveStrategy": "Best",
		  "objectDetectorConfig": {
		    "minObjectSize": 40,
		    "maxObjectSize": 2000,
		    "maxObjects": 20,
		    "confidenceThreshold": 5000,
		    "objectTypesConfiguration": {
		      "detectCar": false,
		      "detectBus": false,
		      "detectTruck": false,
		      "detectMotorcycle": false,
		      "detectBicycle": false,
		      "detectBoat": false,
		      "detectAirplane": false,
		      "detectTrain": false,
		      "detectBird": false,
		      "detectCat": false,
		      "detectDog": false,
		      "detectHorse": false,
		      "detectSheep": false,
		      "detectCow": false,
		      "detectBear": false,
		      "detectElephant": false,
		      "detectGiraffe": false,
		      "detectZebra": false,
		      "detectSuitcase": false,
		      "detectBackpack": false,
		      "detectHandbag": false,
		      "detectUmbrella": false,
		      "detectKnife": false
		    }
		  }
		}
				
				""".formatted(dto.getName(),dto.getSource());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.valueOf("application/json-patch+json"));
	    headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));


		HttpEntity<String> entity = new HttpEntity<>(body, headers);
		
	    try {
            
	        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
	        int statusCode = response.getStatusCode().value();

	        if (statusCode == 201) {
	            return response.getBody(); 
	        } else if (statusCode == 409 || statusCode == 400) {

	            throw new SmartfaceException(response.getBody(), statusCode);
	        } else {
	            throw new SmartfaceException("Unexpected error: " + response.getBody(), statusCode);
	        }

	    } catch (SmartfaceException e) {
	        throw e;
	    } catch (Exception e) {
	        throw new SmartfaceException(e.getMessage(), HttpStatus.BAD_REQUEST.value());
	    }
		
	}
}
