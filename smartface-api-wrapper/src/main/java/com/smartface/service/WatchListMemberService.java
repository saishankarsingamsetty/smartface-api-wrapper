package com.smartface.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartface.application.SmartfaceProperties;
import com.smartface.dto.CreateWatchListMemberDTO;
import com.smartface.exception.SmartfaceException;
import com.smartface.response.ApiResponse;

@Service
public class WatchListMemberService {
	
	@Autowired
	SmartfaceProperties smartfaceProperties;
	
	@Autowired
	RestTemplate restTemplate;

	public boolean deleteWatchListMemberById(String watchListMemberId)throws SmartfaceException
	{
		try {
			String url = smartfaceProperties.getBaseurl() + "WatchlistMembers/"+watchListMemberId;
			
			ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Object.class);
			
			if(response.getStatusCode() == HttpStatus.NO_CONTENT)
			{
				return true;
			}else {
				throw new SmartfaceException("failed to delete the member", 404);
			}
		}catch(Exception e)
		{
			throw new SmartfaceException("failed to delete the member", 404);
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
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("application/json-patch+json"));
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));

		HttpEntity<String> entity = new HttpEntity<>(body, headers);

		ResponseEntity<?> response = restTemplate.postForEntity(url, entity, Object.class);
		
		
			
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
//			System.out.println(title);
			throw new SmartfaceException(title, e.getStatusCode().value());

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public boolean unLinkWatchListMemberToWatchList(String watchListId, String watchListMemberId)
			throws SmartfaceException {
		try {
			String url = smartfaceProperties.getBaseurl() + "/WatchlistMembers/UnlinkFromWatchlist";

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
//			System.out.println(title);
			throw new SmartfaceException(title, e.getStatusCode().value());

		} catch (Exception e) {
//			e.printStackTrace();
			
			return false;
		}
	}
}
