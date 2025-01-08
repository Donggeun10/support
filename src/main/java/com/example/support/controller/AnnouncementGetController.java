package com.example.support.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.support.component.DtoModelAssembler;
import com.example.support.domain.AnnouncementResponse;
import com.example.support.service.AnnouncementReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
	private final DtoModelAssembler dtoModelAssembler;

    @Operation(summary = "모든 공지 사항 목록 조회", responses = {
		@ApiResponse( responseCode = "200", description = "조회된 공지 사항 정보를 반환함."),
		@ApiResponse( responseCode = "401", description = "사용자 정보가 없음")
	}, security = @SecurityRequirement(name = "basicAuth"))
	@GetMapping(value = "/announcements", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionModel<EntityModel<AnnouncementResponse>>> getAllAnnouncements(){
		List<EntityModel<AnnouncementResponse>> announcements = announcementReadService.findAnnouncements().stream().map(dtoModelAssembler::toModel).toList();
		return ResponseEntity.ok(CollectionModel.of(announcements,
													linkTo(methodOn(AnnouncementGetController.class).getAllAnnouncements()).withSelfRel()));
	}

	@Operation(summary = "공지 사항 ID로 조회", responses = {
		@ApiResponse( responseCode = "200", description = "조회된 공지 사항 정보를 반환함."),
		@ApiResponse( responseCode = "401", description = "사용자 정보가 없음"),
		@ApiResponse( responseCode = "404", description = "공지 사항 정보가 없음" )
	}, security = @SecurityRequirement(name = "basicAuth"))
	@GetMapping(value = "/announcement/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel<AnnouncementResponse>> getAnnouncementById(@PathVariable String id) {
		AnnouncementResponse announcementResponse = announcementReadService.findAnnouncementByIdAndIncreaseViewCount(id);
		return ResponseEntity.ok(dtoModelAssembler.toModel(announcementResponse));

	}

	@Operation(summary = "페이지별 공지 사항 목록 조회", responses = {
		@ApiResponse( responseCode = "200", description = "조회된 공지 사항 정보를 반환함"),
		@ApiResponse( responseCode = "400", description = "요청한 페이지에 해당하는 데이터가 없음" ),
		@ApiResponse( responseCode = "401", description = "사용자 정보가 없음")
	}, security = @SecurityRequirement(name = "basicAuth"))
	@GetMapping(value = "/announcements/page", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AnnouncementResponse>> getAnnouncementsByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {

		return ResponseEntity.ok(announcementReadService.findAnnouncementsByPage(page, pageSize));
	}
}
