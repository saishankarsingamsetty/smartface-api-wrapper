package com.smartface.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartface.exception.SmartfaceException;

@Service
public class WatchListService {
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	RestTemplate restTemplate;

	private static final String API_URL = "http://13.233.47.209:8098/api/v1/Watchlists?Ascending=true&PageSize=1000&ShowTotalCount=true";
	
	public boolean isValidWatchListName(String value) {
//		System.out.println("validation method");
		if (value == null || value.trim().isEmpty()) {
			return true;
		}

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(API_URL, String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				JsonNode root = objectMapper.readTree(response.getBody());
				JsonNode items = root.path("items");

				for (JsonNode item : items) {
					String existingName = item.path("displayName").asText();
					if (value.equalsIgnoreCase(existingName)) {
						throw new SmartfaceException("duplicate Watchlist name found",HttpStatus.BAD_REQUEST.value());
					}
				}
			}
		} catch (Exception e) {
//			System.err.println("Error calling watchlist API: " + e.getMessage());
			throw new SmartfaceException("bad request",HttpStatus.BAD_REQUEST.value());
		}

		return true;
	}
}
