package com.smartface.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.smartface.application.SmartfaceProperties;
import com.smartface.response.ApiResponse;

@RequestMapping("/watchlist")
@RestController
public class WatchListController {
	
	@Autowired
	SmartfaceProperties smartfaceProperties;
	
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/fetch")
	public ResponseEntity<?> fetchWatchLists(){
		try {
			
			String url = smartfaceProperties.getBaseurl() + "Watchlists?Ascending=true&ShowTotalCount=true&PageNumber=1&PageSize="+smartfaceProperties.getDefaultPageSize();
			
			Object response = restTemplate.getForObject(url, Object.class);
			
			if(response == null) {
				ApiResponse<?> apiResponse = new ApiResponse<>("non content available", null, HttpStatus.NO_CONTENT.value());
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
}
