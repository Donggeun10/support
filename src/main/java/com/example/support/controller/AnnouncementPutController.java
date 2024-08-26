package com.example.support.controller;

import com.example.support.domain.SuccessResponse;
import com.example.support.entity.Announcement;
import com.example.support.exception.DataSaveException;
import com.example.support.exception.NotFoundAnnouncementException;
import com.example.support.service.AnnouncementUpdateService;
import com.example.support.util.DataUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.sql.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/announcement")
public class AnnouncementPutController {

	private final AnnouncementUpdateService announcementUpdateService;

	public AnnouncementPutController(AnnouncementUpdateService announcementUpdateService) {
		this.announcementUpdateService = announcementUpdateService;
	}

	@Operation(summary = "공지 사항 ID로 변경", responses = {
		@ApiResponse( responseCode = "202", description = "요청 공지 사항 ID에 해당하는 공지 사항 정보 변경 성공" ),
		@ApiResponse( responseCode = "401", description = "사용자 정보가 없음"),
		@ApiResponse( responseCode = "404", description = "공지 사항 정보가 없음" ),
		@ApiResponse( responseCode = "500", description = "데이터 변경 중 오류 발생함" )
	}, security = @SecurityRequirement(name = "basicAuth"))
    @PutMapping(value="/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> putAnnouncementById(@PathVariable String id, @RequestParam String title, @RequestParam String content,
													  @RequestParam(defaultValue = "2024-08-22 00:00:00") String beginDatetime,
													  @RequestParam(defaultValue = "2024-09-22 00:00:00") String endDatetime,
													  Authentication authentication) throws NotFoundAnnouncementException, DataSaveException {

		Announcement announcement = Announcement.builder()
			.title(title)
			.content(content)
			.writer(authentication.getName())
			.beginDatetime(Timestamp.valueOf(beginDatetime))
			.endDatetime(Timestamp.valueOf(endDatetime))
			.build();

		announcementUpdateService.updateAnnouncementById(id, announcement);

		return ResponseEntity.accepted().body(DataUtil.objectToString(new SuccessResponse(String.format("공지사항(%s)이 변경되었습니다.", id))));
	}

}
