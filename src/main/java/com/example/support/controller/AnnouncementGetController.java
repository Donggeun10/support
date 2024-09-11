package com.example.support.controller;

import com.example.support.exception.BadRequestNoContentPageException;
import com.example.support.exception.NotFoundAnnouncementException;
import com.example.support.service.AnnouncementReadService;
import com.example.support.util.DataUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AnnouncementGetController {

	private final AnnouncementReadService announcementReadService;

    @Operation(summary = "모든 공지 사항 목록 조회", responses = {
		@ApiResponse( responseCode = "200", description = "조회된 공지 사항 정보를 반환함."),
		@ApiResponse( responseCode = "401", description = "사용자 정보가 없음")
	}, security = @SecurityRequirement(name = "basicAuth"))
	@GetMapping(value = "/announcements", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getAllAnnouncements(){

		return ResponseEntity.ok(DataUtil.objectToString(announcementReadService.findAnnouncements()));
	}

	@Operation(summary = "공지 사항 ID로 조회", responses = {
		@ApiResponse( responseCode = "200", description = "조회된 공지 사항 정보를 반환함."),
		@ApiResponse( responseCode = "401", description = "사용자 정보가 없음"),
		@ApiResponse( responseCode = "404", description = "공지 사항 정보가 없음" )
	}, security = @SecurityRequirement(name = "basicAuth"))
	@GetMapping(value = "/announcement/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getAnnouncementById(@PathVariable String id) throws NotFoundAnnouncementException {

		return ResponseEntity.ok(DataUtil.objectToString(announcementReadService.findAnnouncementById(id)));
	}

	@Operation(summary = "페이지별 공지 사항 목록 조회", responses = {
		@ApiResponse( responseCode = "200", description = "조회된 공지 사항 정보를 반환함"),
		@ApiResponse( responseCode = "400", description = "요청한 페이지에 해당하는 데이터가 없음" ),
		@ApiResponse( responseCode = "401", description = "사용자 정보가 없음")
	}, security = @SecurityRequirement(name = "basicAuth"))
	@GetMapping(value = "/announcements/page", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getAnnouncementsByPage(@RequestParam int page, @RequestParam int pageSize) throws BadRequestNoContentPageException {

		return ResponseEntity.ok(DataUtil.objectToString(announcementReadService.findAnnouncementsByPage(page, pageSize)));
	}
}
