package com.example.support.advice;

import com.example.support.domain.ErrorResponse;
import com.example.support.exception.BadRequestNoContentPageException;
import com.example.support.exception.DataSaveException;
import com.example.support.exception.NotFoundAnnouncementException;
import com.example.support.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AnnouncementControllerAdvice {
	
	@ExceptionHandler(DataSaveException.class)
	public ResponseEntity<String> serverException(DataSaveException dataSaveException) {

		ErrorResponse errorRes = new ErrorResponse("데이터 저장 중 오류가 발생했습니다.");

		log.error(DataUtil.makeErrorLogMessage(dataSaveException));

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DataUtil.objectToString(errorRes));

	}

	@ExceptionHandler(BadRequestNoContentPageException.class)
	public ResponseEntity<String> serverException(BadRequestNoContentPageException badRequestNoContentPageException) {

		ErrorResponse errorRes = new ErrorResponse(badRequestNoContentPageException.getMessage());

		log.error(DataUtil.makeErrorLogMessage(badRequestNoContentPageException));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DataUtil.objectToString(errorRes));

	}

	@ExceptionHandler(NotFoundAnnouncementException.class)
	public ResponseEntity<String> notFoundAnnouncement(NotFoundAnnouncementException e) {

		ErrorResponse errorRes = new ErrorResponse(e.getMessage());

		log.error(DataUtil.makeErrorLogMessage(e));

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(DataUtil.objectToString(errorRes));

	}

}
