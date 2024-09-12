package com.example.support.advice;

import com.example.support.domain.ErrorResponse;
import com.example.support.exception.BadRequestNoContentPageException;
import com.example.support.exception.DataSaveException;
import com.example.support.exception.NotFoundAnnouncementException;
import com.example.support.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AnnouncementControllerAdvice {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(DataSaveException.class)
	public ErrorResponse serverException(DataSaveException dataSaveException) {

		log.error(DataUtil.makeErrorLogMessage(dataSaveException));
		return new ErrorResponse("데이터 저장 중 오류가 발생했습니다.");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestNoContentPageException.class)
	public ErrorResponse serverException(BadRequestNoContentPageException badRequestNoContentPageException) {

		log.error(DataUtil.makeErrorLogMessage(badRequestNoContentPageException));
		return new ErrorResponse(badRequestNoContentPageException.getMessage());
	}

	@ExceptionHandler(NotFoundAnnouncementException.class)
	public ResponseEntity<ErrorResponse> notFoundAnnouncement(NotFoundAnnouncementException e) {

		ErrorResponse errorRes = new ErrorResponse(e.getMessage());
		log.error(DataUtil.makeErrorLogMessage(e));

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorRes);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> dataIntegrityViolation(DataIntegrityViolationException e) {

		ErrorResponse errorRes = new ErrorResponse(e.getMessage());
		log.error(DataUtil.makeErrorLogMessage(e));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorRes);
	}
}