package com.smartface.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.smartface.application.SmartfaceProperties;
import com.smartface.dto.CreateCameraDTO;
import com.smartface.dto.UpdateCameraDTO;
import com.smartface.entities.Camera;
import com.smartface.exception.SmartfaceException;
import com.smartface.mappers.CameraMapper;
import com.smartface.response.ApiResponse;
import com.smartface.service.CameraService;

@RestController
@RequestMapping("/camera")
public class CameraController {

    private final CameraService cameraService;
	
	@Autowired
	SmartfaceProperties smartfaceProperties;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	CameraMapper cameraMapper ;

    CameraController(CameraService cameraService) {
        this.cameraService = cameraService;
    }

	@GetMapping("/fetch")
	public ResponseEntity<?> fetchAllCameras(){
		String url = smartfaceProperties.getBaseurl()+"Cameras";
		
		try {
			Object response = restTemplate.getForObject(url, Object.class);
			
			if(response == null) {
				ApiResponse<?> apiResponse = new ApiResponse<>("no content found", null, HttpStatus.NO_CONTENT.value());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
			}
			
			ApiResponse<?> apiResponse = new ApiResponse<>("success", response, HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
			
		}
		catch (HttpClientErrorException e) {
            ApiResponse<String> apiResponse = new ApiResponse<>("Client error: " + e.getMessage(), null, e.getStatusCode().value());
            return ResponseEntity.status(e.getStatusCode()).body(apiResponse);

        } catch (HttpServerErrorException e) {
            ApiResponse<String> apiResponse = new ApiResponse<>("Server error: " + e.getResponseBodyAsString(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>("Unexpected error: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addCamera(@RequestBody CreateCameraDTO dto){
		ApiResponse<?> response = null;
		try {
			String result = cameraService.addCamera(dto);
			response = new ApiResponse("success",result,201);
		}
		catch(SmartfaceException e) {
			response = new ApiResponse("failure",e.getMessage(),e.getStatusCode());
		}
		
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@PutMapping("/update")
	public ResponseEntity<ApiResponse<String>> updateCamera(@RequestBody UpdateCameraDTO dto) {
		
		System.out.println("Received UpdateCameraDTO: " + dto);

	    if (dto.getId() == null || dto.getId().isEmpty()) {
	        throw new SmartfaceException("Camera ID is required for update", HttpStatus.BAD_REQUEST.value());
	    }

	    // Fetch the existing entity (from DB)
	    System.out.println("Fetching camera with ID: " + dto.getId());
	    Camera camera = cameraService.fetchCameraById(dto.getId());
	    
	    System.out.println("Fetched camera: ......................" + camera);
	    
	    System.out.println("..............................................................................................");
	    if (camera == null) {
	        throw new SmartfaceException("Camera not found", HttpStatus.NOT_FOUND.value());
	    }

	    // ✅ Merge DTO into entity
	    cameraMapper.updateCameraFromDto(dto, camera);
	    
	    System.out.println("Updated camera entity: " + camera);

	    // Save updated entity
	    String result = cameraService.updateCamera(camera);

	    ApiResponse<String> response = new ApiResponse<>("success", result, HttpStatus.OK.value());
	    return ResponseEntity.ok(response);
	}
	
}
