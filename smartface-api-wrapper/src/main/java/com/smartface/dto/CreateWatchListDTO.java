package com.smartface.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateWatchListDTO {
	
	@NotBlank
	private String displayName;
	
	@NotBlank
	private String fullName;
	
	@Min(0)
	@Max(100)
	private int threshold;
}
