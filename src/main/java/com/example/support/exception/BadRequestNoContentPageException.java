package com.example.support.exception;

/**
 * page / pageSize 기준으로 데이터가 없을 경우 발생
 * */
public class BadRequestNoContentPageException extends RuntimeException {

	public BadRequestNoContentPageException(String message) {
		super(message);
	}

}
