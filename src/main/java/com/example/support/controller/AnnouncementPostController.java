package com.example.support.controller;

import com.example.support.domain.SuccessResponse;
import com.example.support.entity.Announcement;
import com.example.support.exception.DataSaveException;
import com.example.support.exception.NotFoundAnnouncementException;
import com.example.support.service.AnnouncementCreateService;
import com.example.support.util.DataUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.sql.Timestamp;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/announcement")
public class AnnouncementPostController {

	private final AnnouncementCreateService announcementCreateService;

    public AnnouncementPostController(AnnouncementCreateService announcementCreateService) {
        this.announcementCreateService = announcementCreateService;
    }

	@Operation(summary = "공지 사항 신규 등록", responses = {
		@ApiResponse( responseCode = "201", description = "등록 성공" ),
		@ApiResponse( responseCode = "401", description = "사용자 정보가 없음"),
		@ApiResponse( responseCode = "500", description = "등록 중 오류 발생함" )
	}, security = @SecurityRequirement(name = "basicAuth"))
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> postAnnouncement(@RequestParam String title, @RequestParam String content,
												   @RequestParam(defaultValue = "2024-08-22 00:00:00") String beginDatetime,
												   @RequestParam(defaultValue = "2024-09-22 00:00:00") String endDatetime,
												   @RequestPart(value="attached", required=false) List<MultipartFile> files,
												   Authentication authentication) throws DataSaveException {

		Announcement announcement = Announcement.builder()
				.announceId(DataUtil.getCurrentDatetimeStringValue())
				.title(title)
				.content(content)
				.writer(authentication.getName())
				.beginDatetime(Timestamp.valueOf(beginDatetime))
				.endDatetime(Timestamp.valueOf(endDatetime))
				.build();

		announcementCreateService.insertAnnouncement(announcement, files);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(summary = "기존 공지 사항에 대해서 신규 첨부 파일 등록", responses = {
		@ApiResponse( responseCode = "202", description = "첨부 파일 등록 성공" ),
		@ApiResponse( responseCode = "401", description = "사용자 정보가 없음"),
		@ApiResponse( responseCode = "404", description = "존재하지 않은 공지사항ID를 요청함" )
	}, security = @SecurityRequirement(name = "basicAuth"))
	@PostMapping(value="/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> putAnnouncementById(@PathVariable String id, @RequestPart(value="attached") List<MultipartFile> files) throws NotFoundAnnouncementException, DataSaveException {

		announcementCreateService.appendFilesByAnnouncementId(id, files);

		return ResponseEntity.accepted().body(DataUtil.objectToString(new SuccessResponse(String.format("공지사항(%s)이 파일이 추가 되었습니다.", id))));
	}
}
