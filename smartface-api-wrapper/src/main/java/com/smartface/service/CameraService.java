package com.smartface.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartface.SmartfaceApiWrapperApplication;
import com.smartface.application.SmartfaceProperties;
import com.smartface.dto.CreateCameraDTO;
import com.smartface.dto.UpdateCameraDTO;
import com.smartface.entities.Camera;
import com.smartface.entities.ConfigurationEntity;
import com.smartface.exception.SmartfaceException;

@Service
public class CameraService {

	private final SmartfaceApiWrapperApplication smartfaceApiWrapperApplication;

	@Autowired
	SmartfaceProperties smartfaceProperties;

	@Autowired
	RestTemplate restTemplate;
	
	
	@Autowired
	ConfigurationService configurationService;

	CameraService(SmartfaceApiWrapperApplication smartfaceApiWrapperApplication) {
		this.smartfaceApiWrapperApplication = smartfaceApiWrapperApplication;
	}

	public String addCamera(CreateCameraDTO dto) {
		
		ConfigurationEntity configEntity = configurationService.getById(dto.getConfigurationName());
		
		if(configEntity==null) {
			throw new SmartfaceException("configuration name should be in the list", HttpStatus.BAD_REQUEST.value());
		}

		String url = smartfaceProperties.getBaseurl() + "Cameras";

		String body = """
								{
				  "name": "%s",
				  "source": "%s",
				  "enabled": true,
				  "faceDetectorConfig": {
				    "minFaceSize": %s,
				    "maxFaceSize": %s,
				    "maxFaces": %s,
				    "confidenceThreshold": %s
				  },
				  "faceDetectorResourceId": "%s",
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
				    "%s"
				  ],
				  "palmSpoofDetectorResourceIds": [
				    "none"
				  ],
				  "spoofDetectorConfig": {
				    "externalScoreThreshold": 0,
				    "distantLivenessScoreThreshold": %s,
				    "nearbyLivenessScoreThreshold": %s,
				    "distantLivenessConditions": "%s",
				    "nearbyLivenessConditions": "%s"
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

						""".formatted(
								dto.getName()
								, dto.getSource()
								,configEntity.getConfig().get(0).getFaceDetectorConfig().getMinFaceSize().getSelectedValue()
								,configEntity.getConfig().get(0).getFaceDetectorConfig().getMaxFaceSize().getSelectedValue()
								,configEntity.getConfig().get(0).getFaceDetectorConfig().getMaxFaces().getSelectedValue()
								,configEntity.getConfig().get(0).getFaceDetectorConfig().getConfidenceThreshold().getSelectedValue()
								,configEntity.getConfig().get(0).getFaceDetectorResourceId().getSelectedValue()
								,configEntity.getConfig().get(0).getSpoofDetectorResourceIds().getSelectedValue()
								,configEntity.getConfig().get(0).getSpoofDetectorConfig().getDistantLivenessScoreThreshold()
								,configEntity.getConfig().get(0).getSpoofDetectorConfig().getNearbyLivenessScoreThreshold()
								,configEntity.getConfig().get(0).getSpoofDetectorConfig().getDistantLivenessConditions()
								,configEntity.getConfig().get(0).getSpoofDetectorConfig().getNearbyLivenessConditions()
								);

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

	public String updateCamera(Camera camera) {
		String url = smartfaceProperties.getBaseurl() + "Cameras/";

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String body = objectMapper.writeValueAsString(camera);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

			HttpEntity<String> entity = new HttpEntity<>(body, headers);

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

			int statusCode = response.getStatusCode().value();

			if (statusCode == 200) {
				return response.getBody();
			} else if (statusCode == 404) {
				throw new SmartfaceException("Camera not found", 404);
			} else if (statusCode == 400 || statusCode == 409) {
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
	
	
	//fetch camera by id
	public Camera fetchCameraById(String Id)
	{
		try {
			String url = smartfaceProperties.getBaseurl() + "Cameras/" + Id;
			ResponseEntity<Camera> response = restTemplate.getForEntity(url, Camera.class);
			
			System.out.println("respones body:...................."+response.getBody());
			
			if (response.getStatusCode() == HttpStatus.OK) {
				return (Camera) response.getBody();
			} else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new SmartfaceException("Camera not found", 404);
			} else {
				throw new SmartfaceException("Unexpected error: " + response.getBody(), response.getStatusCode().value());
			}
		} catch (SmartfaceException e) {
			throw e;
		} catch (Exception e) {
			throw new SmartfaceException(e.getMessage(), HttpStatus.BAD_REQUEST.value());
		}
	}

}
