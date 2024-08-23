package com.example.support.exception;

/**
 * 공지 사항 정보가 없을 경우 발생
 * */
public class NotFoundAnnouncementException extends RuntimeException {

	public NotFoundAnnouncementException(String message) {
		super(message);
	}

}
