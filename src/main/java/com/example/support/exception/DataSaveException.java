package com.example.support.exception;

/**
 * Request Body 의 데이터를 저장할 때 발생
 * */
public class DataSaveException extends RuntimeException {

	public DataSaveException(String message) {
		super(message);
	}

}
