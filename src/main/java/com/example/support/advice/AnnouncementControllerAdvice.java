package com.example.support.advice;

import com.example.support.exception.BadRequestNoContentPageException;
import com.example.support.exception.DataSaveException;
import com.example.support.exception.NotFoundAnnouncementException;
import com.example.support.util.DataUtil;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AnnouncementControllerAdvice {

	private static final String ERROR_CATEGORY = "errorCategory";
	private static final String TIMESTAMP = "timestamp";

	@ExceptionHandler(DataSaveException.class)
	public ProblemDetail serverException(DataSaveException dataSaveException) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 저장 중 오류가 발생했습니다.");
		problemDetail.setTitle("Data Save Error");
		problemDetail.setProperty(ERROR_CATEGORY, "Save");
		problemDetail.setProperty(TIMESTAMP, Instant.now());

		log.error(DataUtil.makeErrorLogMessage(dataSaveException));

		return problemDetail;
	}

	@ExceptionHandler(BadRequestNoContentPageException.class)
	public ProblemDetail serverException(BadRequestNoContentPageException badRequestNoContentPageException) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NO_CONTENT, badRequestNoContentPageException.getMessage());
		problemDetail.setTitle("Announcement Content page Not Found ");
		problemDetail.setProperty(ERROR_CATEGORY, "Retrieval");
		problemDetail.setProperty(TIMESTAMP, Instant.now());

		log.error(DataUtil.makeErrorLogMessage(badRequestNoContentPageException));

		return problemDetail;
	}

	@ExceptionHandler(NotFoundAnnouncementException.class)
	public ProblemDetail notFoundAnnouncement(NotFoundAnnouncementException e) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
		problemDetail.setTitle("Announcement Not Found ");
		problemDetail.setProperty(ERROR_CATEGORY, "Retrieval");
		problemDetail.setProperty(TIMESTAMP, Instant.now());

		log.error(DataUtil.makeErrorLogMessage(e));

		return problemDetail;
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ProblemDetail dataIntegrityViolation(DataIntegrityViolationException e) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
		problemDetail.setTitle("Data Save Error");
		problemDetail.setProperty(ERROR_CATEGORY, "Save");
		problemDetail.setProperty(TIMESTAMP, Instant.now());

		log.error(DataUtil.makeErrorLogMessage(e));

		return problemDetail;
	}
}