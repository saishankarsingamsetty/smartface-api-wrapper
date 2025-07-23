package com.smartface.validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class DisAllowedWatchListNameValidator implements ConstraintValidator<NotInDisallowedWatchlistNames, String> {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static final String API_URL = "http://13.233.47.209:8098/api/v1/Watchlists?Ascending=true&PageSize=1000&ShowTotalCount=true";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
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
						return false;
					}
				}
			}
		} catch (Exception e) {
//			System.err.println("Error calling watchlist API: " + e.getMessage());

			return false;
		}

		return true;
	}

}
