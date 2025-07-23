package com.smartface.dto;

import java.util.List;

import lombok.Data;

@Data
public class MasterListDTO {
	private String id;
	private String name;
	private String img;
	private List<String> labels;

}
