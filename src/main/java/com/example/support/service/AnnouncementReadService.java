package com.example.support.service;

import com.example.support.component.DataManipulator;
import com.example.support.domain.AnnouncementResponse;
import com.example.support.entity.Announcement;
import com.example.support.exception.BadRequestNoContentPageException;
import com.example.support.exception.NotFoundAnnouncementException;
import com.example.support.repository.AnnouncementRespository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnnouncementReadService {

	private final AnnouncementRespository announcementRespository;
	private final DataManipulator dataManipulator;

	public AnnouncementReadService(AnnouncementRespository announcementRespository, DataManipulator dataManipulator) {
		this.announcementRespository = announcementRespository;
        this.dataManipulator = dataManipulator;
    }

	public List<AnnouncementResponse> findAnnouncements() {
		return dataManipulator.makeAnnouncementResponseList(announcementRespository.findAll());
	}

	public List<AnnouncementResponse> findAnnouncementsByPage(int page, int pageSize) throws BadRequestNoContentPageException {

		PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "announceId"));
		Page<Announcement> announcementList = announcementRespository.findAll(pageRequest);
		if(announcementList.hasContent()) {
			return dataManipulator.makeAnnouncementResponseList(announcementList.toList());
		} else {
			throw new BadRequestNoContentPageException(String.format("요청한 페이지(%d, pageSize:%d)의 데이터는 존재하지 않습니다. ", page, pageSize));
		}

	}

	public AnnouncementResponse findAnnouncementById(String id) throws NotFoundAnnouncementException {

		Optional<Announcement> announcementOpt = announcementRespository.findByAnnounceId(id);

		if(announcementOpt.isPresent()) {
			Announcement announcement = announcementOpt.get();
			announcement.increaseViewCount();
			announcementRespository.save(announcement);
			return dataManipulator.makeAnnouncementResponse(announcement);
		} else {
			throw new NotFoundAnnouncementException(String.format("요청한 ID(%s)의 공지 사항 정보가 없습니다.", id));
		}

	}
}
