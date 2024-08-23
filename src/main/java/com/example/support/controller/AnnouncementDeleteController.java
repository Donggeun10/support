package com.example.support.controller;

import com.example.support.domain.SuccessResponse;
import com.example.support.exception.NotFoundAnnouncementException;
import com.example.support.service.AnnouncementDeleteService;
import com.example.support.util.DataUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/announcement")
public class AnnouncementDeleteController {

	private final AnnouncementDeleteService announcementDeleteService;

	public AnnouncementDeleteController(AnnouncementDeleteService announcementDeleteService) {
		this.announcementDeleteService = announcementDeleteService;
	}

	@Operation(summary = "공지 사항 ID로 삭제", responses = {
		@ApiResponse( responseCode = "202", description = "요청 공지 사항 ID에 해당 하는 공지 사항 삭제 성공" ),
		@ApiResponse( responseCode = "404", description = "공지 사항 정보가 없음" )
	})
	@DeleteMapping(value="/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteAnnouncementById(@PathVariable String id) throws NotFoundAnnouncementException {

		announcementDeleteService.deleteAnnouncementById(id);

		return ResponseEntity.accepted().body(DataUtil.objectToString(new SuccessResponse(String.format("공지 사항(%s)이 삭제 되었습니다.", id))));
	}

	@Operation(summary = "공지 사항 ID와 파일ID 로 파일 삭제", responses = {
		@ApiResponse( responseCode = "202", description = "요청 공지 사항 ID와 파일ID 에 해당 하는 공지 사항 내 첨부 파일 삭제 성공" ),
		@ApiResponse( responseCode = "404", description = "공지 사항 정보 혹은 파일 정보가 없음" )
	})
	@DeleteMapping(value="/id/{id}/file-id/{fileId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteAnnouncementFileById(@PathVariable String id, @PathVariable int fileId) throws NotFoundAnnouncementException {

		announcementDeleteService.deleteAnnouncementByIdAndFileId(id, fileId);

		return ResponseEntity.accepted().body(DataUtil.objectToString(new SuccessResponse(String.format("공지 사항(%s)이 파일(%s)이 삭제 되었습니다.", id, fileId))));
	}
}
