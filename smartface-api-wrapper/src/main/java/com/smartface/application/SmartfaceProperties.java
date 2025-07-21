package com.smartface.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.val;

@PropertySource(value = {"classpath:smartface.properties"})
@Component
@Getter
public class SmartfaceProperties {
 
	@Value("${smartface.api.baseurl}")
	private String baseurl;
	
	@Value("${smartface.page.defaultsize}")
	private int defaultPageSize;
}
