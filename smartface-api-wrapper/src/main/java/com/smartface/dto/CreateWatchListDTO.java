package com.smartface.dto;

import com.smartface.validations.NotInDisallowedWatchlistNames;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateWatchListDTO {
	
	@NotBlank
	@NotInDisallowedWatchlistNames
	private String displayName;
	
	@NotBlank
	private String fullName;
	
	@Min(0)
	@Max(100)
	private int threshold;
}
