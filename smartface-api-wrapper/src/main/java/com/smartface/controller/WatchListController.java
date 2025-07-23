package com.smartface.controller;

import java.util.Map;


import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartface.application.SmartfaceProperties;
import com.smartface.dto.CreateWatchListDTO;
import com.smartface.dto.CreateWatchListMemberDTO;
import com.smartface.exception.SmartfaceException;
import com.smartface.response.ApiResponse;

import jakarta.validation.Valid;

@RequestMapping("/watchlist")
@RestController
public class WatchListController {

	@Autowired
	SmartfaceProperties smartfaceProperties;

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/fetch")
	public ResponseEntity<?> fetchWatchLists() {
		try {

			String url = smartfaceProperties.getBaseurl()
					+ "Watchlists?Ascending=true&ShowTotalCount=true&PageNumber=1&PageSize="
					+ smartfaceProperties.getDefaultPageSize();

			Object response = restTemplate.getForObject(url, Object.class);

			if (response == null) {
				ApiResponse<?> apiResponse = new ApiResponse<>("non content available", null,
						HttpStatus.NO_CONTENT.value());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
			}

			ApiResponse<?> apiResponse = new ApiResponse<>("success", response, HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
		} catch (HttpClientErrorException e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Client error: " + e.getMessage(), null,
					e.getStatusCode().value());
			return ResponseEntity.status(e.getStatusCode()).body(apiResponse);

		} catch (HttpServerErrorException e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Server error: " + e.getResponseBodyAsString(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

		} catch (Exception e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Unexpected error: " + e.getMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
		}

	}



	@PostMapping("/addMember")
	public ResponseEntity<?> addWatchListMember(@RequestBody CreateWatchListMemberDTO createWatchListMemberDTO) {

		String id = null;
		boolean isLinked = false;
		try {
			try {
				
				id = getWatchListMemberId(createWatchListMemberDTO);
//				System.out.println(id);
				createWatchListMemberDTO.setMemberId(id);
			}
			catch(Exception e) {
				ApiResponse<?> response = new ApiResponse<>("failed to create watchlist member", e, HttpStatus.BAD_REQUEST.value());
				return ResponseEntity.badRequest().body(response);
			}

			try {
				
				isLinked = linkWatchListMemberToWatchList(createWatchListMemberDTO.getWatchlistId(),createWatchListMemberDTO.getMemberId());
			}
			catch(Exception e) {
				ApiResponse response = new ApiResponse<>("failed to link to the watchlist", e, HttpStatus.BAD_REQUEST.value());
				return ResponseEntity.badRequest().body(response);
			}
			
			
			ApiResponse response = addImageToWatchListMember(createWatchListMemberDTO);
			return ResponseEntity.status(response.getStatusCode()).body(response);
				

		} catch (Exception e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Error while adding member: " + e.getMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
		}
	}
	
	public ApiResponse<?> addImageToWatchListMember(CreateWatchListMemberDTO createWatchListMemberDTO) {
		
		String url = smartfaceProperties.getBaseurl()+"WatchlistMembers/"+createWatchListMemberDTO.getMemberId()+"/AddNewFace";
		
		String body =
				String.format("""
						{
						  "imageData": {
						    "faceId": null,
						    "data": "%s"
						  },
						  "faceDetectorConfig": {
						    "minFaceSize": 35,
						    "maxFaceSize": 600,
						    "maxFaces": 20,
						    "confidenceThreshold": 450
						  },
						  "faceDetectorResourceId": "%s",
						  "templateGeneratorResourceId": "%s",
						  "faceValidationMode": "predefined"
						}
						""", createWatchListMemberDTO.getImageBase64(), smartfaceProperties.getFaceDetectorResourceId(), smartfaceProperties.getTemplateGeneratorResourceId());
		
		ResponseEntity<?> response = restTemplate.postForEntity(url, body, Object.class);
		
		
			
		ApiResponse<?> apiResponse = new ApiResponse<>(response.getStatusCode().is2xxSuccessful()?"success":"failure", response.getBody(), response.getStatusCode().value());
		
		return apiResponse;
		
		
		
		
	}

	public String getWatchListMemberId(CreateWatchListMemberDTO createWatchListMemberDTO) throws SmartfaceException {
		try {

			String url = smartfaceProperties.getBaseurl() + "WatchlistMembers";

			ResponseEntity response = restTemplate.postForEntity(url, createWatchListMemberDTO, Object.class);

			if (response.getStatusCode() == HttpStatus.CREATED) {
				Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
				return (String) responseBody.get("id");
			} else {
				throw new SmartfaceException("Failed to create watchlist member", response.getStatusCodeValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null; // Handle exceptions as needed
		}
	}

	public boolean linkWatchListMemberToWatchList(String watchListId, String watchListMemberId)
			throws SmartfaceException {
		try {
			String url = smartfaceProperties.getBaseurl() + "/WatchlistMembers/LinkToWatchlist";

			String body = """
					{
					    "watchlistId": "%s",
					    "watchlistMembersIds": ["%s"]
					}
					""".formatted(watchListId, watchListMemberId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(body, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.NO_CONTENT) {
				return true;
			} else {
				throw new SmartfaceException("Unexpected response: " + response.getBody(),
						response.getStatusCodeValue());
			}

		} catch (HttpClientErrorException e) {
			String title = "Client error";
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode json = mapper.readTree(e.getResponseBodyAsString());
				if (json.has("title")) {
					title = json.get("title").asText();
				}
			} catch (Exception ex) {
			}
			System.out.println(title);
			throw new SmartfaceException(title, e.getStatusCode().value());

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@PostMapping("/create")
	public ResponseEntity<?> createWatchList(@Valid @RequestBody CreateWatchListDTO createWatchListDTO,
			BindingResult result) {
		try {
			if (!result.hasErrors()) {

				String url = smartfaceProperties.getBaseurl() + "Watchlists";

				ResponseEntity<?> response = restTemplate.postForEntity(url, createWatchListDTO, Object.class);

				ApiResponse<?> apiResponse = new ApiResponse<>("Watchlist was created successfully", response.getBody(),
						response.getStatusCodeValue());
				return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
			} else {
				List<String> errorMessages = result.getAllErrors().stream()
						.map(error -> error.getDefaultMessage()).collect(Collectors.toList());

				ApiResponse<?> response = new ApiResponse("error creating watchlist",errorMessages,HttpStatus.BAD_REQUEST.value());
				return ResponseEntity.badRequest().body(response);
			}

		} catch (HttpClientErrorException e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Client error", e.getResponseBodyAsString(),
					e.getStatusCode().value());
			return ResponseEntity.status(e.getStatusCode()).body(apiResponse);

		} catch (HttpServerErrorException e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Server error", e.getResponseBodyAsString(),
					e.getStatusCode().value());
			return ResponseEntity.status(e.getStatusCode()).body(apiResponse);

		} catch (Exception e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Unexpected error: " + e.getMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
		}

	}

	@PostMapping("/delete")
	public ResponseEntity<?> deleteWatchList(@RequestBody Map<String, String> request) {
	    try {
	        String watchlistId = request.get("id");
	        if (watchlistId == null || watchlistId.isEmpty()) {
	            return ResponseEntity.badRequest().body(new ApiResponse<>("Watchlist ID is required", null, HttpStatus.BAD_REQUEST.value()));
	        }

	        String url = smartfaceProperties.getBaseurl() + "Watchlists/" + watchlistId;

	        restTemplate.delete(url);

	        ApiResponse<String> apiResponse = new ApiResponse<>(
	                "Watchlist deleted successfully",
	                null,
	                HttpStatus.OK.value()
	        );
	        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

	    } catch (HttpClientErrorException e) {
	        ApiResponse<String> apiResponse = new ApiResponse<>(
	                "Client error",
	                e.getResponseBodyAsString(),
	                e.getStatusCode().value()
	        );
	        return ResponseEntity.status(e.getStatusCode()).body(apiResponse);

	    } catch (HttpServerErrorException e) {
	        ApiResponse<String> apiResponse = new ApiResponse<>(
	                "Server error",
	                e.getResponseBodyAsString(),
	                e.getStatusCode().value()
	        );
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

	    } catch (Exception e) {
	        ApiResponse<String> apiResponse = new ApiResponse<>(
	                "Unexpected error: " + e.getMessage(),
	                null,
	                HttpStatus.INTERNAL_SERVER_ERROR.value()
	        );
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
	    }
	}

}
