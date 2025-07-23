package com.smartface.dto;

import lombok.Data;

@Data
public class CreateWatchListMemberDTO {
	
	private String displayName;
	private String fullName;
	private String note;
	
	private String imageBase64;
	
	private String watchlistId;
}
