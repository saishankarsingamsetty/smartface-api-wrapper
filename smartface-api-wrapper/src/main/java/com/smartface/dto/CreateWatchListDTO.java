package com.smartface.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CreateWatchListDTO {
	
	private String displayName;
	private String fullName;
	
	@Min(0)
	@Max(100)
	private int threshold;
}
