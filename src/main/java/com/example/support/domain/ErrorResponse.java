package com.example.support.domain;

import lombok.Getter;

@Getter
public class ErrorResponse {

	private String errorMessage = "";
	
	public ErrorResponse(String errorMsg) {
		errorMessage = errorMsg;
	}
}
