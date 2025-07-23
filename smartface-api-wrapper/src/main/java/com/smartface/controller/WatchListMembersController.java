package com.smartface.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.smartface.application.SmartfaceProperties;
import com.smartface.dto.DeleteWatchListMemberDto;
import com.smartface.dto.WatchListMemberDTO;
import com.smartface.dto.WatchListMembersRequest;
import com.smartface.exception.SmartfaceException;
import com.smartface.response.ApiResponse;
import com.smartface.service.WatchListMemberService;


@RestController
@RequestMapping("/watchlistmembers")
public class WatchListMembersController {

    private final CameraController cameraController;

	@Autowired
	SmartfaceProperties smartfaceProperties;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	WatchListMemberService watchListMemberService;


    WatchListMembersController(CameraController cameraController) {
        this.cameraController = cameraController;
    }
	
	
	@PostMapping("/fetch")
	public ResponseEntity<?> fetchWatchlistMembersById(@RequestBody WatchListMembersRequest request) {
	    if (request == null || request.getWatchlistId() == null) {
	        ApiResponse<?> response = new ApiResponse<>("Bad Request", null, HttpStatus.BAD_REQUEST.value());
	        return ResponseEntity.badRequest().body(response);
	    }

	    try {
	        String membersUrl = smartfaceProperties.getBaseurl() +
	            "Watchlists/" + request.getWatchlistId() +
	            "/WatchlistMembers?Ascending=true&PageNumber=1&PageSize=" +
	            smartfaceProperties.getDefaultPageSize() +
	            "&ShowTotalCount=true";

	        Map<String, Object> result = restTemplate.getForObject(membersUrl, Map.class);
	        List<Map<String, Object>> items = (List<Map<String, Object>>) result.get("items");

	        List<WatchListMemberDTO> members = new ArrayList<>();

	        for (Map<String, Object> item : items) {
	            WatchListMemberDTO member = new WatchListMemberDTO();
	            member.setId((String) item.get("id"));
	            member.setDisplayName((String) item.get("displayName"));

	            // Fetch face details
	            String facesUrl = smartfaceProperties.getBaseurl() +
	                "WatchlistMembers/" + member.getId() +
	                "/Faces?Ascending=true&ShowTotalCount=true";

	            Map<String, Object> faceData = restTemplate.getForObject(facesUrl, Map.class);
	            List<Map<String, Object>> faceItems = (List<Map<String, Object>>) faceData.get("items");

	            if (faceItems != null && !faceItems.isEmpty()) {
	                String imageDataId = (String) faceItems.get(0).get("imageDataId");

	                byte[] imageBytes = fetchImageBytes(imageDataId);
	                if (imageBytes != null) {
	                    String base64 = Base64.getEncoder().encodeToString(imageBytes);
	                    member.setImageBase64("data:image/jpeg;base64," + base64);
	                }
	            }

	            members.add(member);
	        }

	        ApiResponse<List<WatchListMemberDTO>> apiResponse =
	                new ApiResponse<>("Success", members, HttpStatus.OK.value());

	        return ResponseEntity.ok(apiResponse);

	    } catch (HttpClientErrorException e) {
	        return ResponseEntity.status(e.getStatusCode())
	                .body(new ApiResponse<>(e.getMessage(), null, e.getRawStatusCode()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse<>(e.getMessage(), null, 500));
	    }
	}
	
	
	private byte[] fetchImageBytes(String imageDataId) {
	    try {
	        String imageUrl = smartfaceProperties.getBaseurl() + "Images/" + imageDataId;
	        return restTemplate.getForObject(imageUrl, byte[].class);
	    } catch (Exception e) {
	        return new byte[0];
	    }
	}
	
	@PostMapping("/delete")
	public ResponseEntity<?> deleteWatchListMemeberById(@RequestBody DeleteWatchListMemberDto dto){
		
		try {
			boolean deletionStatus = watchListMemberService.deleteWatchListMemberById(dto.getMemberId());
			ApiResponse<?> apiResponse = new ApiResponse<>("success", null, 200);
			return ResponseEntity.ok(apiResponse);
		}
		catch(SmartfaceException e) {
			ApiResponse<?> apiResponse = new ApiResponse<>("failed to delete the member", null, e.getStatusCode());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
		}
		
		
		
	}

	
	
}
